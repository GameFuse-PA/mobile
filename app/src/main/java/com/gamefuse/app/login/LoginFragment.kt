package com.gamefuse.app.login

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.gamefuse.app.R
import com.gamefuse.app.api.ApiClient
import com.gamefuse.app.api.model.request.LoginUser
import com.gamefuse.app.myFriendsList.FriendsListActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private var loginButton: Button? = null
    private var problemFieldEmail: ImageView? = null
    private var problemFieldPassword: ImageView? = null
    private var editTextLogin: EditText? = null
    private var editTextPassword: EditText? = null
    private var textViewError: TextView? = null
    private var forgotPassword: TextView? = null
    private var progressBar: ProgressBar? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_login, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.loginButton = view.findViewById(R.id.buttonLogin)
        this.problemFieldEmail = view.findViewById(R.id.problem_field_email)
        this.problemFieldPassword = view.findViewById(R.id.problem_field_password)
        this.editTextLogin = view.findViewById(R.id.editTextLogin);
        this.editTextPassword = view.findViewById(R.id.editTextPassword);
        this.textViewError = view.findViewById(R.id.textViewError);
        this.forgotPassword = view.findViewById(R.id.forgotPassword);
        this.progressBar = view.findViewById(R.id.progressBar)

        this.loginButton?.setOnClickListener {
            val request = LoginUser(
                email = this.editTextLogin?.text.toString(),
                password = this.editTextPassword?.text.toString()
            )
            this.login(request)
        }

        this.forgotPassword?.paintFlags =
            forgotPassword?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!;


        this.problemFieldEmail?.visibility = View.INVISIBLE
        this.problemFieldPassword?.visibility = View.INVISIBLE
    }

    private fun login(request: LoginUser) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                startLoading()
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.login(request) }

                if (response.isSuccessful && response.body() != null) {
                    Connect.authToken = Gson().toJson(response.body())
                    val intent = Intent(context, FriendsListActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    stopLoading()
                    problemFieldEmail?.visibility = View.VISIBLE
                    problemFieldPassword?.visibility = View.VISIBLE
                    editTextLogin?.setBackgroundResource(R.drawable.custom_wrong_input_field)
                    editTextPassword?.setBackgroundResource(R.drawable.custom_wrong_input_field)
                    textViewError?.setText(R.string.invalid_credentials)
                }
            } catch (e: Exception) {
                stopLoading()
                Toast.makeText(context, getString(R.string.api_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLoading() {
        progressBar?.visibility = ProgressBar.VISIBLE
        loginButton?.isEnabled = false
    }

    private fun stopLoading() {
        progressBar?.visibility = ProgressBar.GONE
        loginButton?.isEnabled = true
    }

}
