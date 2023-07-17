package com.gamefuse.app.conversation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.Connect
import com.gamefuse.app.R
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.api.model.response.MessageModel
import com.google.gson.Gson
import com.squareup.picasso.Picasso


class ConversationAdapter(private val messages: List<MessageModel>
) :
    RecyclerView.Adapter<ConversationAdapter.MessageViewHolder>() {
    private val loggedUserInfos = Gson().fromJson(Connect.authToken, LoginResponse::class.java)

    abstract class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract val messageAvatarImageView: ImageView;
        abstract val messageUsernameTextView: TextView;
        abstract val messageContentTextView: TextView;
    }
    class MyMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override val messageAvatarImageView: ImageView = itemView.findViewById(R.id.senderAvatarImageView)
        override val messageUsernameTextView: TextView = itemView.findViewById(R.id.senderUsernameTextView)
        override val messageContentTextView: TextView = itemView.findViewById(R.id.senderMessageContentTextView)
    }

    class RecipientMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override val messageAvatarImageView: ImageView = itemView.findViewById(R.id.recipientAvatarImageView)
        override val messageUsernameTextView: TextView = itemView.findViewById(R.id.recipientUsernameTextView)
        override val messageContentTextView: TextView = itemView.findViewById(R.id.recipientMessageContentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.conversation_item, parent, false)
        println("j'affiche l'item " + itemView)
        //TODO: voir comment checker si c'est mon message ou non
        return MyMessageViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        if(message.from.id == loggedUserInfos.user._id) {
            //TODO : MY MESSAGE
        } else {
            //TODO : OTHER'S MESSAGE
        }
        val user = message.from
        user.let {
            Picasso.get()
                .load(user.avatar?.location)
                .into(holder.messageAvatarImageView)

            holder.messageUsernameTextView.text = user.username
            holder.messageContentTextView.text = message.content
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}
