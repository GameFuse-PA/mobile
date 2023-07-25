package com.gamefuse.app.myConversations

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class MyConversationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_my_conversations)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, MyConversationsFragment())
            .commit()
    }
}
