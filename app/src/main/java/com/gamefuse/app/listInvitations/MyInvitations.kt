package com.gamefuse.app.listInvitations

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class MyInvitations : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_list)
        val nextFragment = MyInvitationsFragment()
        val bundle = Bundle()
        nextFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, nextFragment).commit()
    }
}