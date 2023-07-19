package com.gamefuse.app.conversation

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
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.api.model.response.MessageModel
import com.gamefuse.app.conversation.adapter.ConversationAdapter
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
            .inflate(R.layout.fragment_conversation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.conversationMessagesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val conversationId = requireActivity().intent.getStringExtra("conversationId");

        println("ma conv id : " + conversationId)
        CoroutineScope(Dispatchers.Main).launch {
            startLoading()
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getConversation("Bearer " + user.access_token, conversationId.toString())
                }
                println(response)
                if (response.isSuccessful) {
                    val conversation = response.body()
                    println("display conv : " + conversation)
                    if (conversation != null) {
                        println("coucou" + conversation)
                        val adapter = ConversationAdapter(conversation.messages as MutableList<MessageModel>, recyclerView)
                        recyclerView.adapter = adapter
                        adapter.scrollToBottom()
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

    private fun startLoading() {
        progressBar?.visibility = ProgressBar.VISIBLE
    }

    private fun stopLoading() {
        progressBar?.visibility = ProgressBar.GONE
    }


}


https://github.com/heyletscode/Chat-App-In-Android-And-NodeJS-Using-WebSockets/blob/master/AndroidApp/app/src/main/java/com/heyletscode/chattutorial/ChatActivity.java#L49