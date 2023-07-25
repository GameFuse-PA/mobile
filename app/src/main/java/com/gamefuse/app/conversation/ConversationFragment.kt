package com.gamefuse.app.conversation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.Connect
import com.gamefuse.app.R
import com.gamefuse.app.api.ApiClient
import com.gamefuse.app.api.model.request.MessageForChat
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.api.model.response.MessageModel
import com.gamefuse.app.api.model.response.User
import com.gamefuse.app.conversation.adapter.ConversationAdapter
import com.gamefuse.app.myConversations.MyConversationsActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ConversationFragment : Fragment() {

    private var progressBar: ProgressBar? = null
    private val user = Gson().fromJson(Connect.authToken, LoginResponse::class.java)
    var messageEditText: TextInputEditText? = null;
    var users: List<User>? = null;
    var conversationId: String? = null
    var recyclerView: RecyclerView? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_conversation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeConversation: ImageView = view.findViewById(R.id.close_conversation)
        val sendButton: MaterialButton = view.findViewById(R.id.sendButton)
        val reload: ImageView = view.findViewById(R.id.reload)
        messageEditText = view.findViewById(R.id.messageEditText);


        conversationId = requireActivity().intent.getStringExtra("conversationId");

        closeConversation.setOnClickListener {
            val intent = Intent(requireContext(), MyConversationsActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        sendButton.setOnClickListener {
            sendMessage();
        }

        reload.setOnClickListener(){
            this.loadConversation();
        }
        this.loadConversation();
    }

    private fun loadConversation() {
        recyclerView = view?.findViewById(R.id.conversationMessagesRecyclerView)

        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        CoroutineScope(Dispatchers.Main).launch {
            startLoading()
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getConversation(
                        "Bearer " + user.access_token,
                        conversationId.toString()
                    )
                }
                if (response.isSuccessful) {
                    val conversation = response.body()
                    if (conversation != null) {
                        users = conversation.users;
                        val adapter = recyclerView?.let {
                            ConversationAdapter(
                                conversation.messages as MutableList<MessageModel>,
                                it
                            )
                        }
                        recyclerView?.adapter = adapter
                        adapter?.scrollToBottom()
                    } else {
                        Toast.makeText(
                            context,
                            "Erreur lors de la récupération des conversations.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Une erreur s'est produite lors de la récupération de vos conversations",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "nono", Toast.LENGTH_LONG).show()
                e.message?.let { Log.e("Erreur de requête", it) }
            } finally {
                stopLoading()
            }
        }
    }

    private fun sendMessage() {
        CoroutineScope(Dispatchers.Main).launch {
            startLoading()
            try {
                var to: String? = null;
                if (users?.get(0)?.id.equals(user.user._id)) {
                    to = users?.get(1)?.id
                } else {
                    to = users?.get(0)?.id
                }
                if (conversationId != null && to != null) {
                    val messageToSend = MessageForChat(
                        content = messageEditText?.text.toString(),
                        conversationId = conversationId!!,
                        to = to,
                    )
                    messageEditText?.setText("")
                    val response = withContext(Dispatchers.IO) {
                        ApiClient.apiService.sendMessage(
                            "Bearer " + user.access_token,
                            messageToSend
                        )
                    }
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Message envoyé",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Une erreur s'est produite lors de l'envoi du message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (_: Exception) {
            } finally {
                stopLoading()
            }
        }
    }

    private fun startLoading() {
        progressBar?.visibility = ProgressBar.VISIBLE
    }

    private fun stopLoading() {
        progressBar?.visibility = ProgressBar.GONE
    }


}