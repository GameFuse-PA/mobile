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
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.R
import com.gamefuse.app.myFriendsList.adapter.FriendsAdapter
import com.gamefuse.app.myFriendsList.dto.ListFriendsDto
import com.gamefuse.app.service.ReloadFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendsListFragment: Fragment(), ReloadFragment {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_friends_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_friends)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL)
        )

        val listFriends: MutableList<ListFriendsDto> = mutableListOf()

        val imageNoFriends: ImageView = view.findViewById(R.id.empty_list_image)
        val textNoFriends: TextView = view.findViewById(R.id.empty_list_text)


        GlobalScope.launch(Dispatchers.Main) {
            try {
                val request = withContext(Dispatchers.IO) {
                    Request.getFriends(Connect.authToken)
                }
                val obj = request.friends
                if (request.friends.isEmpty()){
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

            }catch (e: Exception){
                Toast.makeText(context, "Impossible de récupérer vos amis, Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.message?.let { Log.e("Erreur requête", it) }
            }
        }

        return view

    }

    override fun reloadFragment() {
        val fragment = FriendsListFragment()

        val fragmentManager = parentFragmentManager

        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.container, fragment)

        transaction.addToBackStack(null)

        transaction.commit()
    }

    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()


}