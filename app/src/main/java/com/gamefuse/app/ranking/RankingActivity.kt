package com.gamefuse.app.ranking

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_ranking)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, RankingFragment())
            .commit()
    }
}
