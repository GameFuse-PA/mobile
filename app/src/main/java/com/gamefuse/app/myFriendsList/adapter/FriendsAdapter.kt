package com.gamefuse.app.myFriendsList.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.R
import com.gamefuse.app.myFriendsList.dto.ListFriendsDto
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.io.IOException
import java.net.URL
import java.util.concurrent.Executors

class FriendsAdapter(private val friends: List<ListFriendsDto>): RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val username: TextView = itemView.findViewById(R.id.username_list_friends)
        val profilPic: ImageView = itemView.findViewById(R.id.pp_user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.friends_list_component, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friends[position]
        holder.username.text = friend.username
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val url = URL(friend.image)
                val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                holder.profilPic.post {
                    holder.profilPic.setImageBitmap(bitmap)
                    Picasso.get().load(url.toString()).resize(300, 200).transform(CropCircleTransformation()).into(holder.profilPic)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}