package com.gamefuse.app.myFriendsList

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.R
import com.gamefuse.app.myFriendsList.adapter.FriendsAdapter
import com.gamefuse.app.myFriendsList.dto.ListFriendsDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendsListFragment: Fragment() {


     companion object {
          fun newInstance(): FriendsListFragment {
                return FriendsListFragment()
          }
     }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_friends_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_friends)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val listFriends: MutableList<ListFriendsDto> = mutableListOf()

        val imageNoFriends: ImageView = view.findViewById(R.id.empty_list_image)
        val textNoFriends: TextView = view.findViewById(R.id.empty_list_text)


        GlobalScope.launch {
            try {
                val request = withContext(Dispatchers.IO) {
                    Request.getFriends("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Ik1hbnUiLCJzdWIiOiI2NDU3YzA4ZmE4ZDQ2YTlkMWJhOTViNzIiLCJpYXQiOjE2ODc0NTU4MTcsImV4cCI6MTY4NzU0MjIxN30.9lMQeyoGoy_ijSIbLBajJH9Qt8d0zAS0hDeXvGTAD9c")
                }
                val obj = request.friends
                if (request.friends.isEmpty()){
                    imageNoFriends.visibility = View.VISIBLE
                    textNoFriends.visibility = View.VISIBLE
                    return@launch
                }
                for (friend in obj){
                    imageNoFriends.visibility = View.INVISIBLE
                    textNoFriends.visibility = View.INVISIBLE
                    listFriends.add(ListFriendsDto(friend.id, friend.name, friend.username, friend.avatar.location))
                }

                val adapter = FriendsAdapter(listFriends)
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

            }catch (e: Exception){
                Toast.makeText(activity, "Impossible de récupérer vos amis, Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.message?.let { Log.e("Erreur requête", it) }
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState)

    }

    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

}