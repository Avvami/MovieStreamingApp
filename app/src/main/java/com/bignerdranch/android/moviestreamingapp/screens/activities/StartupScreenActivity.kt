package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.ActivityStartupScreenBinding

class StartupScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartupScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartupScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginLinkBtn.setOnClickListener {
            startActivity(Intent(this@StartupScreenActivity, LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        binding.registerLinkBtn.setOnClickListener{
            startActivity(Intent(this@StartupScreenActivity, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top)
        }
    }
}