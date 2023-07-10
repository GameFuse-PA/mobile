package com.gamefuse.app.searchFriend

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.gamefuse.app.searchFriend.adapter.SearchFriendAdapter
import com.gamefuse.app.searchFriend.dto.SearchFriendDto
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFriendFragment: Fragment() {

    private val token = Gson().fromJson(Connect.authToken, LoginResponse::class.java)
    private var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_search_friend,
            container,
            false
        )
        val noResult = view.findViewById<TextView>(R.id.no_results_search)
        noResult.visibility = View.GONE
        val searchText: EditText = view.findViewById(R.id.value_search_user)
        val buttonSearch: LinearLayout = view.findViewById(R.id.search_user_button)
        val listUsers: MutableList<SearchFriendDto> = mutableListOf()

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_search_results)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL)
        )

        val quitButton = view.findViewById<ImageView>(R.id.cross_quit_search_friend)

        quitButton.setOnClickListener {
            activity?.finish()
        }

        buttonSearch.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                recyclerView.visibility = View.GONE
                noResult.visibility = View.GONE
                startLoading()

                try{
                    val request = withContext(Dispatchers.IO) {
                        ApiClient.apiService.searchUser("Bearer " + token.access_token, searchText.text.toString())
                    }
                    if (request.isSuccessful){
                        if (request.body()!!.isEmpty()){
                            stopLoading()
                            val noResultText = getString(R.string.no_results_search, searchText.text.toString())
                            noResult.text = noResultText
                            noResult.visibility = View.VISIBLE
                            return@launch
                        }
                        listUsers.clear()
                        for (res in request.body()!!){
                            val user = res.user
                            val isFriend = res.isFriend
                            val image = user.avatar?.location
                            listUsers.add(SearchFriendDto(user.id, user.name, user.username, image, isFriend))

                        }

                        val adapter = SearchFriendAdapter(listUsers)
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
                        recyclerView.visibility = View.VISIBLE
                        stopLoading()
                    }else{
                        stopLoading()
                        noResult.visibility = View.VISIBLE
                        Toast.makeText(context, "Impossible de récupérer les utilisateurs", Toast.LENGTH_LONG).show()
                    }
                }catch (e: Exception) {
                    stopLoading()
                    noResult.visibility = View.VISIBLE
                    Toast.makeText(context, "Erreur lors de la connexion", Toast.LENGTH_LONG).show()
                }
            }
        }

        return view
    }

    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun startLoading() {
        progressBar?.visibility = ProgressBar.VISIBLE
    }

    private fun stopLoading() {
        progressBar?.visibility = ProgressBar.GONE
    }


}