package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.moviestreamingapp.R
import kotlinx.android.synthetic.main.activity_startup_screen.*

class StartupScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup_screen)

        loginLink.setOnClickListener {
            startActivity(Intent(this@StartupScreenActivity, LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        registerLink.setOnClickListener{
            startActivity(Intent(this@StartupScreenActivity, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top)
        }
    }
}