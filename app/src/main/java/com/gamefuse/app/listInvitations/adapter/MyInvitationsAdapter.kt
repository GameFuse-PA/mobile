package com.gamefuse.app.listInvitations.adapter

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
import com.gamefuse.app.api.model.request.Invitations
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.api.model.response.User
import com.gamefuse.app.listInvitations.dto.MyInvitationsDto
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

class MyInvitationsAdapter(private val invitations: MutableList<MyInvitationsDto>): RecyclerView.Adapter<MyInvitationsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.username_list_invitations)
        val profilPic: ImageView = itemView.findViewById(R.id.pp_user)
        val acceptInvite: ImageView = itemView.findViewById(R.id.accept_invitation)
        val refuseInvite: ImageView = itemView.findViewById(R.id.refuse_invitation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.invitations_list_component, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return invitations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val invitation = invitations[position]
        holder.userName.text = invitation.username
        holder.acceptInvite.setOnClickListener {
            val positiveButton = { _: DialogInterface, _: Int -> acceptInvite(holder, invitation.id)}
            val negativeButton = { _: DialogInterface, _: Int -> print("")}
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle(holder.itemView.resources.getString(R.string.add_friend))
            builder.setMessage(holder.itemView.resources.getString(R.string.accept_invite, invitation.username))
            builder.setPositiveButton(holder.itemView.resources.getString(R.string.yes), DialogInterface.OnClickListener(function = positiveButton))
            builder.setNegativeButton(holder.itemView.resources.getString(R.string.no), negativeButton)
            builder.show()
        }
        holder.refuseInvite.setOnClickListener {
            val positiveButton = { _: DialogInterface, _: Int -> refuseInvite(holder, invitation.id)}
            val negativeButton = { _: DialogInterface, _: Int -> print("")}
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle(holder.itemView.resources.getString(R.string.add_friend))
            builder.setMessage(holder.itemView.resources.getString(R.string.refuse_invite, invitation.username))
            builder.setPositiveButton(holder.itemView.resources.getString(R.string.yes), DialogInterface.OnClickListener(function = positiveButton))
            builder.setNegativeButton(holder.itemView.resources.getString(R.string.no), negativeButton)
            builder.show()
        }

        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                if (invitation.image == null){
                    holder.profilPic.post {
                        val bitmap = BitmapFactory.decodeResource(holder.itemView.context.resources, R.drawable.photo_avatar_profil)
                        holder.profilPic.setImageBitmap(bitmap)
                        Picasso.get().load(R.drawable.photo_avatar_profil).resize(300, 300).transform(
                            CropCircleTransformation()
                        ).into(holder.profilPic)
                    }
                    return@execute
                }
                val url = URL(invitation.image)
                val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                holder.profilPic.post {
                    holder.profilPic.setImageBitmap(bitmap)
                    Picasso.get().load(url.toString()).resize(300, 200).transform(
                        CropCircleTransformation()
                    ).into(holder.profilPic)
                }
            } catch (e: IOException) {
                Toast.makeText(holder.itemView.context, holder.itemView.resources.getString(R.string.error_picture), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun acceptInvite(holder: ViewHolder, id: String) {

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.acceptInvitation("Bearer " + Gson().fromJson(Connect.authToken, LoginResponse::class.java).access_token, Invitations(id))
                }
                if (response.isSuccessful){
                    val index = invitations.indexOfFirst { it.id == id }
                    invitations.removeAt(index)
                    notifyItemRemoved(index)
                    notifyItemRangeChanged(index, invitations.size)
                    Toast.makeText(holder.itemView.context, holder.itemView.resources.getString(R.string.invite_accepted), Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(holder.itemView.context, holder.itemView.resources.getString(R.string.api_error), Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception){
                Toast.makeText(holder.itemView.context, holder.itemView.resources.getString(R.string.api_error), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun refuseInvite(holder: ViewHolder, id: String) {

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.refuseInvitation("Bearer " + Gson().fromJson(Connect.authToken, LoginResponse::class.java).access_token, Invitations(id))
                }
                if (response.isSuccessful){
                    val index = invitations.indexOfFirst { it.id == id }
                    invitations.removeAt(index)
                    notifyItemRemoved(index)
                    notifyItemRangeChanged(index, invitations.size)
                    Toast.makeText(holder.itemView.context, holder.itemView.resources.getString(R.string.invite_refused), Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(holder.itemView.context, holder.itemView.resources.getString(R.string.api_error), Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception){
                Toast.makeText(holder.itemView.context, holder.itemView.resources.getString(R.string.api_error), Toast.LENGTH_LONG).show()
            }
        }
    }


}