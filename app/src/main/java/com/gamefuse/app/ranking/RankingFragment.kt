package com.gamefuse.app.ranking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gamefuse.app.Connect
import com.gamefuse.app.R
import com.gamefuse.app.api.ApiClient
import com.gamefuse.app.api.model.request.ForgotPassword
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.ranking.adapter.RankingAdapter
import com.gamefuse.app.register.RegisterActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RankingFragment : Fragment() {

    private var ranking: String? = null;
    private var progressBar: ProgressBar? = null

    private val user = Gson().fromJson(Connect.authToken, LoginResponse::class.java)
    val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_ranking, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun getRanking() {
        CoroutineScope(Dispatchers.Main).launch {
            startLoading()
            try {
                val response =
                    withContext(Dispatchers.IO) { ApiClient.apiService.getScoreBoard("Bearer " + user.access_token) }
                if (response.isSuccessful) {
                    println(response)
                    println("classement reçu")
                    val ranking = response.body()
                    if(ranking != null) {
                        val adapter = RankingAdapter(ranking)
                        recyclerView.adapter = adapter
                    }
                } else {
                    stopLoading()
                    Toast.makeText(context, getString(R.string.api_error), Toast.LENGTH_LONG).show()
                    println("Erreur")
                }
                stopLoading()
            } catch (e: Exception) {
                stopLoading()
                Toast.makeText(context, getString(R.string.api_error), Toast.LENGTH_SHORT).show()
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
