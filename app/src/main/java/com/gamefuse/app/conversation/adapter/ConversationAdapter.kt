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


class ConversationAdapter(private val messages: MutableList<MessageModel>, private val recyclerView: RecyclerView) :
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

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 0
        private const val VIEW_TYPE_OTHERS_MESSAGE = 1
    }


    class RecipientMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override val messageAvatarImageView: ImageView = itemView.findViewById(R.id.recipientAvatarImageView)
        override val messageUsernameTextView: TextView = itemView.findViewById(R.id.recipientUsernameTextView)
        override val messageContentTextView: TextView = itemView.findViewById(R.id.recipientMessageContentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_MY_MESSAGE -> {
                val myMessageView = inflater.inflate(R.layout.my_message_item, parent, false)
                MyMessageViewHolder(myMessageView)
            }
            VIEW_TYPE_OTHERS_MESSAGE -> {
                val recipientMessageView = inflater.inflate(R.layout.message_item, parent, false)
                RecipientMessageViewHolder(recipientMessageView)
            }
            else -> throw IllegalArgumentException("Erreur lors du choix de type de message: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.from.id == loggedUserInfos.user._id) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHERS_MESSAGE
        }
    }

    fun scrollToBottom() {
        if (messages.isNotEmpty()) {
            recyclerView.scrollToPosition(messages.size - 1)
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
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

    fun addMessage(message: MessageModel) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
        recyclerView.scrollToPosition(messages.size - 1)
    }
}
