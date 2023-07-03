package com.gamefuse.app.myFriendsList.adapter

import Connect
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.R
import com.gamefuse.app.api.ApiClient
import com.gamefuse.app.myFriendsList.dto.ListFriendsDto
import com.gamefuse.app.myFriendsList.service.ApiFriendsInterface
import com.gamefuse.app.service.ReloadFragment
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import java.util.concurrent.Executors

class FriendsAdapter(private val friends: List<ListFriendsDto>, private val apiFriendsInterface: ApiFriendsInterface): RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val username: TextView = itemView.findViewById(R.id.username_list_friends)
        val profilPic: ImageView = itemView.findViewById(R.id.pp_user)
        val addRemoveFriend: ImageView = itemView.findViewById(R.id.add_remove_user)
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
        holder.addRemoveFriend.setImageResource(R.drawable.delete_friend)
        holder.addRemoveFriend.setOnClickListener {
            val positiveButton = { _: DialogInterface, _: Int -> apiFriendsInterface.deleteFriend(friend.id)}
            val negativeButton = { _: DialogInterface, _: Int -> print("")}
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Suppression d'un ami")
            builder.setMessage("Voulez vous vraiment supprimer " + friend.username + " de vos amis ?")
            builder.setPositiveButton("Oui", DialogInterface.OnClickListener(function = positiveButton))
            builder.setNegativeButton("Non", negativeButton)
            builder.show()
        }
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