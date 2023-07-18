package com.gamefuse.app.myConversations.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.Connect
import com.gamefuse.app.R
import com.gamefuse.app.api.model.response.ConversationModelWithoutMessages
import com.gamefuse.app.api.model.response.LoginResponse
import com.google.gson.Gson
import com.squareup.picasso.Picasso


class MyConversationsAdapter(private val conversations: List<ConversationModelWithoutMessages>, private val onConversationClickListener: OnConversationClickListener
) :
    RecyclerView.Adapter<MyConversationsAdapter.ConversationViewHolder>() {
    private val loggedUserInfos = Gson().fromJson(Connect.authToken, LoginResponse::class.java)

    class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
    }

    interface OnConversationClickListener {
        fun onConversationClick(conversationId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.conversation_item, parent, false)
        return ConversationViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]
        var indexTarget = 0;
        if(conversation.users.get(0).id.equals(loggedUserInfos.user._id)) {
            indexTarget = 1;
        } else {
            indexTarget = 0;
        }
        val user = conversation.users[indexTarget]
        user.let {
            Picasso.get()
                .load(user.avatar?.location)
                .into(holder.avatarImageView)

            holder.usernameTextView.text = user.username

            holder.itemView.setOnClickListener {
                onConversationClickListener.onConversationClick(conversation.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return conversations.size
    }
}
