package com.gamefuse.app.listInvitations

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.Connect
import com.gamefuse.app.R
import com.gamefuse.app.api.ApiClient
import com.gamefuse.app.api.model.response.InvitationsResponse
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.listInvitations.adapter.MyInvitationsAdapter
import com.gamefuse.app.listInvitations.dto.MyInvitationsDto
import com.gamefuse.app.myFriendsList.dto.ListFriendsDto
import com.gamefuse.app.service.ReloadFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyInvitationsFragment : Fragment() {

    private var progressBar: ProgressBar? = null
    private val token = Gson().fromJson(Connect.authToken, LoginResponse::class.java)
    private val listInvitations: MutableList<MyInvitationsDto> = mutableListOf()
    private lateinit var textNoInvitations: TextView
    private lateinit var quitButton: ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_invitations, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_invitations)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL)
        )

        textNoInvitations = view.findViewById(R.id.empty_list_text)
        quitButton = view.findViewById(R.id.cross_quit_invitations)

        quitButton.setOnClickListener {
            activity?.finish()
        }

        textNoInvitations.visibility = View.INVISIBLE

        getInvitations(recyclerView)

        return view

    }


    private fun getInvitations(recyclerView: RecyclerView) {

        CoroutineScope(Dispatchers.Main).launch {
            startLoading()
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getInvitations("Bearer " + token.access_token)
                }
                if (response.isSuccessful) {
                    val invitations = response.body()
                    if (invitations.isNullOrEmpty()) {
                        textNoInvitations.visibility = View.VISIBLE
                        stopLoading()
                        return@launch
                    }
                    for (invitation in invitations) {
                        if (invitation.sender.id == token.user._id){
                            continue
                        }
                        val image = invitation.sender.avatar?.location
                        listInvitations.add(MyInvitationsDto(invitation.sender.id, invitation.sender.username, image))
                    }
                    if (listInvitations.isEmpty()) {
                        textNoInvitations.visibility = View.VISIBLE
                        stopLoading()
                        return@launch
                    }
                    val adapter = MyInvitationsAdapter(listInvitations)
                    recyclerView.adapter = adapter
                    recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                        ) {
                            super.getItemOffsets(outRect, view, parent, state)
                            outRect.bottom = 12.dpToPx()
                        }
                    })
                    stopLoading()
                }else{
                    stopLoading()
                    Toast.makeText(context, getString(R.string.error_get_friend_request), Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception){
                stopLoading()
                Toast.makeText(context, getString(R.string.api_error), Toast.LENGTH_LONG).show()
            }

        }

    }


    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()


    private fun startLoading() {
        progressBar?.visibility = ProgressBar.VISIBLE
    }

    private fun stopLoading() {
        progressBar?.visibility = ProgressBar.GONE
    }


}