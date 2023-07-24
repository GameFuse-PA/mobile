package com.gamefuse.app.conversation.adapter

import android.util.Log
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
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class ConversationAdapter(private val messages: MutableList<MessageModel>, private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<ConversationAdapter.MessageViewHolder>() {
    private val loggedUserInfos = Gson().fromJson(Connect.authToken, LoginResponse::class.java)

    abstract class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract val messageAvatarImageView: ImageView;
        abstract val messageUsernameTextView: TextView;
        abstract val messageContentTextView: TextView;
        abstract val messageDate: TextView;
    }
    class MyMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override val messageAvatarImageView: ImageView = itemView.findViewById(R.id.senderAvatarImageView)
        override val messageUsernameTextView: TextView = itemView.findViewById(R.id.senderUsernameTextView)
        override val messageContentTextView: TextView = itemView.findViewById(R.id.senderMessageContentTextView)
        override val messageDate: TextView = itemView.findViewById(R.id.messageDate)
    }

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 0
        private const val VIEW_TYPE_OTHERS_MESSAGE = 1
    }


    class RecipientMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        override val messageAvatarImageView: ImageView = itemView.findViewById(R.id.recipientAvatarImageView)
        override val messageUsernameTextView: TextView = itemView.findViewById(R.id.recipientUsernameTextView)
        override val messageContentTextView: TextView = itemView.findViewById(R.id.recipientMessageContentTextView)
        override val messageDate: TextView = itemView.findViewById(R.id.messageDate)
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
                .transform(
                    CropCircleTransformation())
                .into(holder.messageAvatarImageView)

            val formattedDate = convertDateFormat(message.date.toString())
            println("coucou" + formattedDate)
            holder.messageUsernameTextView.text = user.username
            holder.messageContentTextView.text = message.content
            holder.messageDate.text = formattedDate
        }
    }

    fun convertDateFormat(inputDate: String): String {
        var outputDate = ""
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE)

        try {
            val date = inputFormat.parse(inputDate)
            val timeZoneFrance = TimeZone.getTimeZone("Europe/Paris")
            outputFormat.timeZone = timeZoneFrance
            outputDate = date?.let { outputFormat.format(it) }.toString()
        } catch (e: Exception) {
            println("Erreur lors du parsing de date")
        }

        return outputDate
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
