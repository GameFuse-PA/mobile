package com.gamefuse.app.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, RegisterFragment())
            .commit()
    }
}
