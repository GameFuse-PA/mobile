package com.gamefuse.app.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, LoginFragment())
            .commit()
    }
}
