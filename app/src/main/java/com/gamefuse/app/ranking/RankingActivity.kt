package com.gamefuse.app.ranking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, RankingFragment())
            .commit()
    }
}
