package com.gamefuse.app.conversation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class ConversationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, ConversationFragment())
            .commit()
    }
}
