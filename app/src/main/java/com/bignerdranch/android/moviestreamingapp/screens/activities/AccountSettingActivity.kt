package com.bignerdranch.android.moviestreamingapp.screens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.moviestreamingapp.R
import kotlinx.android.synthetic.main.activity_account_setting.*

class AccountSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)

        accountSettingGroupBack.setOnClickListener() {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}