package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.moviestreamingapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile_nmore.*

class ProfileNMoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_nmore)

        profileNMoreGroupBack.setOnClickListener() {
            onBackPressed()
        }

        signOut.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@ProfileNMoreActivity, StartupScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        editProfileGroup.setOnClickListener() {
            startActivity(Intent(this@ProfileNMoreActivity, ProfileEditActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        notificationGroup.setOnClickListener() {
            startActivity(Intent(this@ProfileNMoreActivity, NotificationsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        myListGroup.setOnClickListener() {
            startActivity(Intent(this@ProfileNMoreActivity, MyListActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        accountGroup.setOnClickListener() {
            startActivity(Intent(this@ProfileNMoreActivity, AccountSettingActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        aboutGroup.setOnClickListener() {
            startActivity(Intent(this@ProfileNMoreActivity, AboutAppActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}