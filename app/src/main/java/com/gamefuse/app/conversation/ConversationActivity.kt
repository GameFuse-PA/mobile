package com.gamefuse.app.conversation

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class ConversationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_conversation)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, ConversationFragment())
            .commit()
    }
}
