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
import com.gamefuse.app.Connect
import com.gamefuse.app.R
import com.gamefuse.app.api.ApiClient
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.searchFriend.dto.SearchFriendDto
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import java.util.concurrent.Executors

class SearchFriendAdapter(
    private val friends: List<SearchFriendDto>
): RecyclerView.Adapter<SearchFriendAdapter.ViewHolder>() {
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
        if (friend.isFriend || friend.isInvited){
            holder.addRemoveFriend.setImageResource(R.drawable.user_in_friends_list)
        }else{
            holder.addRemoveFriend.setImageResource(R.drawable.add_friend)
            holder.addRemoveFriend.setOnClickListener {
                val positiveButton = { _: DialogInterface, _: Int -> addFriend(holder, friend.id)}
                val negativeButton = { _: DialogInterface, _: Int -> print("")}
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setTitle("Ajout d'un ami")
                builder.setMessage("Voulez vous vraiment ajouter " + friend.username + " à votre liste d'amis ?")
                builder.setPositiveButton("Oui", DialogInterface.OnClickListener(positiveButton))
                builder.setNegativeButton("Non", negativeButton)
                builder.show()
            }
        }
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                if (friend.image == null){
                    holder.profilPic.post {
                        val bitmap = BitmapFactory.decodeResource(holder.itemView.context.resources, R.drawable.photo_avatar_profil)
                        holder.profilPic.setImageBitmap(bitmap)
                        Picasso.get().load(R.drawable.photo_avatar_profil).resize(300, 300).transform(
                            CropCircleTransformation()
                        ).into(holder.profilPic)
                    }
                    return@execute
                }
                val url = URL(friend.image)
                val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                holder.profilPic.post {
                    holder.profilPic.setImageBitmap(bitmap)
                    Picasso.get().load(url.toString()).resize(300, 200).transform(
                        CropCircleTransformation()
                    ).into(holder.profilPic)
                }
            } catch (e: IOException) {
                Toast.makeText(holder.itemView.context, "Erreur lors du chargement de l'image", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addFriend(holder: ViewHolder, id: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try{
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.addFriend("Bearer " + Gson().fromJson(Connect.authToken, LoginResponse::class.java).access_token, id)
                }
                if (response.isSuccessful){
                    holder.addRemoveFriend.setImageResource(R.drawable.user_in_friends_list)
                    holder.addRemoveFriend.isClickable = false
                    Toast.makeText(holder.itemView.context, "Demande envoyé", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(holder.itemView.context, "Une demande à déjà été envoyé ou une erreur est survenue", Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception){
                Toast.makeText(holder.itemView.context, "Erreur lors de la requête", Toast.LENGTH_LONG).show()
            }
        }
    }

}