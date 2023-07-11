package com.gamefuse.app.myFriendsList

import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
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
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.listInvitations.MyInvitations
import com.gamefuse.app.myFriendsList.adapter.FriendsAdapter
import com.gamefuse.app.myFriendsList.dto.ListFriendsDto
import com.gamefuse.app.myFriendsList.service.ApiFriendsInterface
import com.gamefuse.app.searchFriend.SearchFriend
import com.gamefuse.app.service.ReloadFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendsListFragment: Fragment(), ReloadFragment, ApiFriendsInterface {

    private var progressBar: ProgressBar? = null
    private val token = Gson().fromJson(Connect.authToken, LoginResponse::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_friends, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_friends)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL)
        )
        val searchFriendButton = view.findViewById<ImageView>(R.id.add_friend_button)
        val invitationsButton = view.findViewById<ImageView>(R.id.my_invitations)

        val listFriends: MutableList<ListFriendsDto> = mutableListOf()

        val imageNoFriends: ImageView = view.findViewById(R.id.empty_list_image)
        val textNoFriends: TextView = view.findViewById(R.id.empty_list_text)

        imageNoFriends.visibility = View.INVISIBLE
        textNoFriends.visibility = View.INVISIBLE
        searchFriendButton.setOnClickListener {
            val intent = Intent(requireContext(), SearchFriend::class.java)
            startActivity(intent)
        }
        invitationsButton.setOnClickListener {
            val intent = Intent(requireContext(), MyInvitations::class.java)
            startActivity(intent)
        }

        getFriends(listFriends, imageNoFriends, textNoFriends, recyclerView)

        return view

    }

    private fun getFriends(listFriends: MutableList<ListFriendsDto>, imageNoFriends: ImageView, textNoFriends: TextView, recyclerView: RecyclerView){

        CoroutineScope(Dispatchers.Main).launch {
            startLoading()

            try {
                val request = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getFriends("Bearer " + token.access_token)
                }
                if (request.isSuccessful) {
                    val obj = request.body()?.friends
                    if (obj.isNullOrEmpty()){
                        imageNoFriends.visibility = View.VISIBLE
                        textNoFriends.visibility = View.VISIBLE
                        return@launch
                    }

                    for (friend in obj){
                        val image: String = if (friend.avatar != null){
                            friend.avatar!!.location
                        }else{
                            "https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png"
                        }
                        imageNoFriends.visibility = View.INVISIBLE
                        textNoFriends.visibility = View.INVISIBLE
                        listFriends.add(ListFriendsDto(friend.id, friend.name, friend.username, image))
                    }
                    val adapter = FriendsAdapter(listFriends, this@FriendsListFragment)
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
                    Toast.makeText(context, "Impossible de récupérer vos amis", Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception){
                stopLoading()
                Toast.makeText(context, getString(R.string.api_error), Toast.LENGTH_LONG).show()
                e.message?.let { Log.e("Erreur requête", it) }
            }
        }

    }

    override fun deleteFriend(id: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try{
                val request = withContext(Dispatchers.IO) {
                    ApiClient.apiService.deleteFriend("Bearer " + token.access_token, id)
                }
                if (request.isSuccessful){
                    reloadFragment()
                    return@launch
                }else{
                    Toast.makeText(context, "Erreur lors de la suppression de l'ami", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                e.message?.let { Log.e("Erreur requête", it) }
            }
        }
    }

    override fun reloadFragment() {
        val fragment = FriendsListFragment()

        val fragmentManager = parentFragmentManager

        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.containerFragment, fragment)

        transaction.addToBackStack(null)

        transaction.commit()
    }

    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()


    private fun startLoading() {
        progressBar?.visibility = ProgressBar.VISIBLE
    }

    private fun stopLoading() {
        progressBar?.visibility = ProgressBar.GONE
    }

}