package com.gamefuse.app.profil

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.gamefuse.app.R

class ProfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        val nextFragment = ProfilFragment()
        val bundle = Bundle()
        nextFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, nextFragment).commit()
    }
}