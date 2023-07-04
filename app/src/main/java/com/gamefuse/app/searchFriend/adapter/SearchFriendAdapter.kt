package com.gamefuse.app.searchFriend.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.R
import com.gamefuse.app.searchFriend.dto.SearchFriendDto
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.io.IOException
import java.net.URL
import java.util.concurrent.Executors

class SearchFriendAdapter(private val friends: List<SearchFriendDto>): RecyclerView.Adapter<SearchFriendAdapter.ViewHolder>() {
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
        if (friend.isFriend){
            holder.addRemoveFriend.setImageResource(R.drawable.user_in_friends_list)
        }else{
            holder.addRemoveFriend.setImageResource(R.drawable.add_friend)
            holder.addRemoveFriend.setOnClickListener {
                val positiveButton = { _: DialogInterface, _: Int -> Toast.makeText(holder.itemView.context , android.R.string.yes, Toast.LENGTH_SHORT).show()}
                val negativeButton = { _: DialogInterface, _: Int -> Toast.makeText(holder.itemView.context , android.R.string.no, Toast.LENGTH_SHORT).show()}
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setTitle("Ajout d'un ami")
                builder.setMessage("Voulez vous vraiment ajouter " + friend.username + " à votre liste d'amis ?")
                builder.setPositiveButton("Oui", DialogInterface.OnClickListener(function = positiveButton))
                builder.setNegativeButton("Non", negativeButton)
                builder.show()
            }
        }
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val url = URL(friend.image)
                val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                holder.profilPic.post {
                    holder.profilPic.setImageBitmap(bitmap)
                    Picasso.get().load(url.toString()).resize(300, 200).transform(
                        CropCircleTransformation()
                    ).into(holder.profilPic)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}