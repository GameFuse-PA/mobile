package com.gamefuse.app.conversation

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
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
import com.gamefuse.app.api.model.request.LoginUser
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.conversation.adapter.ConversationAdapter
import com.gamefuse.app.forgotPassword.ForgotPasswordActivity
import com.gamefuse.app.myConversations.MyConversationsActivity
import com.gamefuse.app.myConversations.adapter.MyConversationsAdapter
import com.gamefuse.app.register.RegisterActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ConversationFragment : Fragment() {

    private var progressBar: ProgressBar? = null
    private val user = Gson().fromJson(Connect.authToken, LoginResponse::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.conversationMessagesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val conversationId = //TODO: récupérer la conversation id depuis la précédente activité

        CoroutineScope(Dispatchers.Main).launch {
            startLoading()
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getConversation("Bearer " + user.access_token, conversationId)
                }
                if (response.isSuccessful) {
                    val conversation = response.body()
                    if (conversation != null) {
                        val adapter = ConversationAdapter(conversation.messages)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(
                            context,
                            "Vous n'avez aucune conversation.",
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

    private fun startLoading() {
        progressBar?.visibility = ProgressBar.VISIBLE
    }

    private fun stopLoading() {
        progressBar?.visibility = ProgressBar.GONE
    }


}
