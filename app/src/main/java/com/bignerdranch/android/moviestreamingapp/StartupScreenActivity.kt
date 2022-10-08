package com.bignerdranch.android.moviestreamingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_startup_screen.*

class StartupScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup_screen)

        loginLink.setOnClickListener {
            startActivity(Intent(this@StartupScreenActivity, LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.nothing_x)
        }

        signupLink.setOnClickListener{
            startActivity(Intent(this@StartupScreenActivity, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_in_top, R.anim.nothing_y)
        }
    }
}