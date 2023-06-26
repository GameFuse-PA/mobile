package com.gamefuse.app.login

import Connect
import LoginResponse
import LoginUser
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gamefuse.app.R
import com.gamefuse.app.homePage.HomePageActivity
import com.gamefuse.app.homePage.HomePageFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton: Button = view.findViewById(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val loginEditText: EditText = view.findViewById(R.id.editTextLogin)
            val passwordEditText: EditText = view.findViewById(R.id.editTextPassword)
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response: LoginResponse = withContext(Dispatchers.IO) {
                        Request.login(LoginUser(login, password))
                    }
                    Connect.authToken = response.access_token;
                    println("coucou" + Connect.authToken)
                    val intent = Intent(requireContext(), HomePageActivity::class.java)
                    startActivity(intent)



                } catch (e: Exception) {
                    val errorMessage = e.message
                    val errorTextView: TextView = view.findViewById(R.id.textViewError)
                    errorTextView.text = errorMessage                }
            }
        }
    }

}
