package com.gamefuse.app.login

import Connect
import LoginResponse
import LoginUser
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gamefuse.app.R
import com.gamefuse.app.homePage.HomePageActivity
import com.gamefuse.app.homePage.HomePageFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.lang.Exception

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    @SuppressLint("CutPasteId")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton: Button = view.findViewById(R.id.buttonLogin)
        val problemFieldEmail = view.findViewById<ImageView>(R.id.problem_field_email)
        val problemFieldPassword = view.findViewById<ImageView>(R.id.problem_field_password)
        val editTextLogin = view.findViewById<EditText>(R.id.editTextLogin);
        val editTextPassword = view.findViewById<EditText>(R.id.editTextPassword);
        val textViewError = view.findViewById<TextView>(R.id.textViewError);
        val forgotPassword = view.findViewById<TextView>(R.id.forgotPassword);

        forgotPassword.paintFlags = forgotPassword.paintFlags or Paint.UNDERLINE_TEXT_FLAG;

        problemFieldEmail.visibility = View.INVISIBLE
        problemFieldPassword.visibility = View.INVISIBLE

        loginButton.setOnClickListener {
            val loginEditText: EditText = view.findViewById(R.id.editTextLogin)
            val passwordEditText: EditText = view.findViewById(R.id.editTextPassword)
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response: LoginResponse? = withContext(Dispatchers.IO) {
                        withTimeoutOrNull(5000) {
                            Request.login(LoginUser(login, password))
                        }
                    }

                    if (response != null) {
                        Connect.authToken = response.access_token
                        val intent = Intent(requireContext(), HomePageActivity::class.java)
                        startActivity(intent)
                    } else {
                        problemFieldEmail.visibility = View.VISIBLE
                        problemFieldPassword.visibility = View.VISIBLE
                        editTextLogin.setBackgroundResource(R.drawable.custom_wrong_input_field)
                        editTextPassword.setBackgroundResource(R.drawable.custom_wrong_input_field)
                        passwordEditText.setBackgroundResource(R.drawable.custom_wrong_input_field)
                        textViewError.setText("Service unaviable")
                    }
                } catch (e: TimeoutCancellationException) {
                    problemFieldEmail.visibility = View.VISIBLE
                    problemFieldPassword.visibility = View.VISIBLE
                    editTextLogin.setBackgroundResource(R.drawable.custom_wrong_input_field)
                    editTextPassword.setBackgroundResource(R.drawable.custom_wrong_input_field)
                    passwordEditText.setBackgroundResource(R.drawable.custom_wrong_input_field)
                    textViewError.setText("Service unaviable")
                }

                catch (e: Exception) {
                    problemFieldEmail.visibility = View.VISIBLE
                    problemFieldPassword.visibility = View.VISIBLE
                    editTextLogin.setBackgroundResource(R.drawable.custom_wrong_input_field)
                    editTextPassword.setBackgroundResource(R.drawable.custom_wrong_input_field)
                    passwordEditText.setBackgroundResource(R.drawable.custom_wrong_input_field)
                    textViewError.text = "Identifiants invalides"                }
            }
        }
    }

}
