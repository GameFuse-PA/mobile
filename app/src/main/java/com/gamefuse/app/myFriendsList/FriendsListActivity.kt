package com.gamefuse.app.myFriendsList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.gamefuse.app.R

class FriendsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_list)
        val nextFragment = FriendsListFragment()
        val bundle = Bundle()
        nextFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, nextFragment).commit()
    }
}