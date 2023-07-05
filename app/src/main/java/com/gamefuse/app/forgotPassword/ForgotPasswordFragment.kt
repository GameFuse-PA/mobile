package com.gamefuse.app.forgotPassword

import Connect
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.gamefuse.app.R
import com.gamefuse.app.api.ApiClient
import com.gamefuse.app.api.model.request.ForgotPassword
import com.gamefuse.app.login.LoginActivity
import com.gamefuse.app.register.RegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordFragment : Fragment() {

    private var loginButton: Button? = null
    private var subscribeButton: Button? = null
    private var problemFieldEmail: ImageView? = null
    private var editTextLogin: EditText? = null
    private var textViewError: TextView? = null
    private var progressBar: ProgressBar? = null
    private var buttonForgotPassword: Button? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_forgot_password, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.loginButton = view.findViewById(R.id.buttonLogin)
        this.subscribeButton = view.findViewById(R.id.buttonSubscribe)
        this.problemFieldEmail = view.findViewById(R.id.problem_field_email)
        this.editTextLogin = view.findViewById(R.id.editTextLogin);
        this.textViewError = view.findViewById(R.id.textViewError);
        this.progressBar = view.findViewById(R.id.progressBar)
        this.buttonForgotPassword = view.findViewById(R.id.buttonForgotPassword)

        this.loginButton?.setOnClickListener {
            this.activity?.finish();
        }

        this.subscribeButton?.setOnClickListener {
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }

        this.buttonForgotPassword?.setOnClickListener {
            val request = ForgotPassword(
                email = this.editTextLogin?.text.toString(),
            )
            this.forgotPassword(request)
        }


        this.problemFieldEmail?.visibility = View.INVISIBLE
    }

    private fun forgotPassword(request: ForgotPassword) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                startLoading()
                withContext(Dispatchers.IO) { ApiClient.apiService.forgotPassword(request) }
                Toast.makeText(context, R.string.mail_sent_if_exist, Toast.LENGTH_SHORT).show()
                stopLoading()
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
