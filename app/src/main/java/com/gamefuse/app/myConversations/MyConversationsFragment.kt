package com.gamefuse.app.myConversations

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
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.conversation.ConversationActivity
import com.gamefuse.app.myConversations.adapter.MyConversationsAdapter
import com.gamefuse.app.register.RegisterActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MyConversationsFragment : Fragment(), MyConversationsAdapter.OnConversationClickListener {

    private var progressBar: ProgressBar? = null
    private val user = Gson().fromJson(Connect.authToken, LoginResponse::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_my_conversations, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.Main).launch {
            startLoading()
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getConversations("Bearer " + user.access_token)
                }
                if (response.isSuccessful) {
                    val conversations = response.body()
                    if (conversations != null) {
                        val adapter = MyConversationsAdapter(conversations, this@MyConversationsFragment)
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

    override fun onConversationClick(conversationId: String) {
        val intent = Intent(requireContext(), ConversationActivity::class.java)
        intent.putExtra("conversationId", conversationId)
        startActivity(intent)
    }

}
