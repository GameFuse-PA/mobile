package com.gamefuse.app.homePage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R


class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomePageFragment.newInstance())
                .commit()
        }
    }
}