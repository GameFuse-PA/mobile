package com.gamefuse.app.searchFriend

import Connect
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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.R
import com.gamefuse.app.Request
import com.gamefuse.app.searchFriend.adapter.SearchFriendAdapter
import com.gamefuse.app.searchFriend.dto.SearchFriendDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFriendFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.activity_search_friend,
            container,
            false
        )
        val noResult = TextView(context)
        val searchText: EditText = view.findViewById(R.id.value_search_user)
        val buttonSearch: LinearLayout = view.findViewById(R.id.search_user_button)
        val listUsers: MutableList<SearchFriendDto> = mutableListOf()

        val recyclerView : RecyclerView? = activity?.let { RecyclerView(it) }
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL)
            )
        }

        val resultLayout = view.findViewById<ViewGroup>(R.id.search_results)
        val quitButton = view.findViewById<ImageView>(R.id.cross_quit_search_friend)

        quitButton.setOnClickListener {
            activity?.finish()
        }

        buttonSearch.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                resultLayout.removeAllViews()

                try{
                    val request = withContext(Dispatchers.IO) {
                        Request.searchUser(Connect.authToken, searchText.text.toString())
                    }

                    if (request.isEmpty()){
                        val noResultText = getString(R.string.no_results_search, searchText.text.toString())
                        noResult.text = noResultText
                        noResult.textSize = 25F
                        noResult.gravity = Gravity.CENTER
                        noResult.setTextColor(Color.BLACK)
                        resultLayout.addView(noResult)
                        resultLayout.requestLayout()
                        return@launch
                    }

                    for (user in request){
                        val image: String = if (user.avatar != null){
                            user.avatar!!.location
                        }else{
                            "https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png"
                        }
                        listUsers.add(SearchFriendDto(user.id, user.name, user.username, image))

                    }

                    val adapter = SearchFriendAdapter(listUsers)
                    if (recyclerView != null) {
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
                        resultLayout.addView(recyclerView)
                        resultLayout.requestLayout()
                        return@launch
                    }

                    Toast.makeText(context, "Erreur lors de l'affichage de la liste d'ami", Toast.LENGTH_LONG).show()
                }catch (e: Exception) {
                    Toast.makeText(context, "Erreur lors de la connexion", Toast.LENGTH_LONG).show()
                    e.message?.let { Log.e("Erreur requête", it) }
                }
            }
        }

        return view
    }

    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}