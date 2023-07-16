package com.gamefuse.app.myConversations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class MyConversationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_conversations)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, MyConversationsFragment())
            .commit()
    }
}
