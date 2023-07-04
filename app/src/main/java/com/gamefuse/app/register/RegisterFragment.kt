package com.gamefuse.app.register

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.gamefuse.app.Connect
import com.gamefuse.app.R
import com.gamefuse.app.api.ApiClient
import com.gamefuse.app.api.model.request.RegisterUser
import com.gamefuse.app.api.model.response.ErrorResponse
import com.gamefuse.app.api.model.response.ErrorResponseWithArrayMessage
import com.gamefuse.app.login.LoginActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private var loginButton: Button? = null
    private var registerButton: Button? = null
    private var problemFieldNickname: ImageView? = null
    private var problemFieldEmail: ImageView? = null
    private var problemFieldPassword: ImageView? = null
    private var problemFieldPasswordConfirmation: ImageView? = null
    private var editTextNickname: EditText? = null
    private var editTextLogin: EditText? = null
    private var editTextPassword: EditText? = null
    private var editTextPasswordConfirmation: EditText? = null
    private var textViewError: TextView? = null
    private var forgotPassword: TextView? = null
    private var progressBar: ProgressBar? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_register, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.loginButton = view.findViewById(R.id.buttonLogin)
        this.registerButton = view.findViewById(R.id.buttonRegister)
        this.problemFieldNickname = view.findViewById(R.id.problem_field_nickname)
        this.problemFieldEmail = view.findViewById(R.id.problem_field_email)
        this.problemFieldPassword = view.findViewById(R.id.problem_field_password)
        this.problemFieldPasswordConfirmation = view.findViewById(R.id.problem_field_password_confirmation)
        this.editTextNickname = view.findViewById(R.id.editTextNickName);
        this.editTextLogin = view.findViewById(R.id.editTextLogin);
        this.editTextPassword = view.findViewById(R.id.editTextPassword);
        this.editTextPasswordConfirmation = view.findViewById(R.id.editTextPasswordConfirmation);
        this.textViewError = view.findViewById(R.id.textViewError);
        this.progressBar = view.findViewById(R.id.progressBar)

        this.registerButton?.setOnClickListener {
            val request = RegisterUser(
                email = this.editTextLogin?.text.toString(),
                password = this.editTextPassword?.text.toString(),
                username = this.editTextNickname?.text.toString()
            )
            this.register(request)
        }

        this.loginButton?.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        this.forgotPassword?.paintFlags =
            forgotPassword?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!;


        this.problemFieldNickname?.visibility = View.INVISIBLE
        this.problemFieldEmail?.visibility = View.INVISIBLE
        this.problemFieldPassword?.visibility = View.INVISIBLE
        this.problemFieldPasswordConfirmation?.visibility = View.INVISIBLE
    }

    private fun resetOverLay() {
        this.editTextNickname?.setBackgroundResource(0)
        this.editTextLogin?.setBackgroundResource(0)
        this.editTextPassword?.setBackgroundResource(0)
        this.editTextPasswordConfirmation?.setBackgroundResource(0)
        this.problemFieldNickname?.visibility = View.INVISIBLE
        this.problemFieldEmail?.visibility = View.INVISIBLE
        this.problemFieldPassword?.visibility = View.INVISIBLE
        this.problemFieldPasswordConfirmation?.visibility = View.INVISIBLE
        this.textViewError?.setText(null);
    }

    private fun register(request: RegisterUser) {
        this.resetOverLay()
        if(editTextPassword?.text.toString() != editTextPasswordConfirmation?.text.toString()){
            editTextPassword?.setBackgroundResource(R.drawable.custom_wrong_input_field)
            editTextPasswordConfirmation?.setBackgroundResource(R.drawable.custom_wrong_input_field)
            problemFieldPassword?.visibility = View.VISIBLE
            problemFieldPasswordConfirmation?.visibility = View.VISIBLE
            textViewError?.setText(R.string.not_corresponding_passwords)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    startLoading()
                    val response = withContext(Dispatchers.IO) { ApiClient.apiService.register(request) }

                    if (response.isSuccessful && response.body() != null) {
                        Connect.authToken = response.body().toString()
                        Toast.makeText(context, "Registered", Toast.LENGTH_SHORT).show()

                        stopLoading()
                    } else {
                        stopLoading()
                        problemFieldEmail?.visibility = View.VISIBLE
                        problemFieldPassword?.visibility = View.VISIBLE
                        editTextNickname?.setBackgroundResource(R.drawable.custom_wrong_input_field)
                        editTextLogin?.setBackgroundResource(R.drawable.custom_wrong_input_field)
                        editTextPassword?.setBackgroundResource(R.drawable.custom_wrong_input_field)
                        editTextPasswordConfirmation?.setBackgroundResource(R.drawable.custom_wrong_input_field)

                        val errorBody =  response.errorBody()?.string();
                        if(errorBody != null) {
                            val gson = Gson();
                            try {
                                var errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                                textViewError?.setText(errorResponse.message)
                            } catch (e: java.lang.Exception) {
                                var errorResponse = gson.fromJson(errorBody, ErrorResponseWithArrayMessage::class.java)
                                textViewError?.setText(errorResponse.message[0])
                            }

                        }else {
                            textViewError?.setText(R.string.api_error)
                        }
                    }
                } catch (e: Exception) {
                    println(e)
                    stopLoading()
                    textViewError?.setText(R.string.api_error)
                    Toast.makeText(context, getString(R.string.api_error), Toast.LENGTH_SHORT).show()
                }
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
