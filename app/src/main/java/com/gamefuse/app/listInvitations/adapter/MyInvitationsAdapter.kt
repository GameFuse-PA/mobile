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
import com.gamefuse.app.R
import com.gamefuse.app.listInvitations.dto.MyInvitationsDto
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
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
            val positiveButton = { _: DialogInterface, _: Int -> print("")}
            val negativeButton = { _: DialogInterface, _: Int -> print("")}
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle(holder.itemView.resources.getString(R.string.add_friend))
            builder.setMessage(holder.itemView.resources.getString(R.string.accept_invite, invitation.username))
            builder.setPositiveButton(holder.itemView.resources.getString(R.string.yes), DialogInterface.OnClickListener(function = positiveButton))
            builder.setNegativeButton(holder.itemView.resources.getString(R.string.no), negativeButton)
            builder.show()
        }
        holder.refuseInvite.setOnClickListener {
            val positiveButton = { _: DialogInterface, _: Int -> print("")}
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
}