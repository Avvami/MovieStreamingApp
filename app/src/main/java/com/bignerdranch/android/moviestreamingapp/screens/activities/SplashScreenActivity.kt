package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bignerdranch.android.moviestreamingapp.data.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbRef = FirebaseDatabase.getInstance().reference

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this@SplashScreenActivity, StartupScreenActivity::class.java))
            finish()
        } else {

            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }
    }
}