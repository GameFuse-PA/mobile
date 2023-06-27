package com.gamefuse.app.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LoginFragment.newInstance())
            .commit()
    }
}
