package com.gamefuse.app.searchFriend

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class SearchFriend : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_friend)
        setContentView(R.layout.container)
        val nextFragment = SearchFriendFragment()
        val bundle = Bundle()
        nextFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, nextFragment).commit()
    }
}