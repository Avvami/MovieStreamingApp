package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.moviestreamingapp.R
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile_nmore.*

class ProfileNMoreActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_nmore)

        dbRef = FirebaseDatabase.getInstance().reference
        val userRef = dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid!!)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val profileImageRef = snapshot.child("profile_image")
                    if (profileImageRef.exists()) {
                        Glide.with(applicationContext).load(profileImageRef.value).into(profileImageStroke)
                    } else {
                        //No profile image
                    }
                    usernameTV.text = snapshot.child("username").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Do nothing
            }

        })

        profileNMoreGroupBack.setOnClickListener {
            onBackPressed()
        }

        signOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@ProfileNMoreActivity, StartupScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        editProfileGroup.setOnClickListener {
            startActivity(Intent(this@ProfileNMoreActivity, ProfileEditActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        notificationGroup.setOnClickListener {
            startActivity(Intent(this@ProfileNMoreActivity, NotificationsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        myListGroup.setOnClickListener {
            startActivity(Intent(this@ProfileNMoreActivity, MyListActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        accountGroup.setOnClickListener {
            startActivity(Intent(this@ProfileNMoreActivity, AccountSettingActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        aboutGroup.setOnClickListener {
            startActivity(Intent(this@ProfileNMoreActivity, AboutAppActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}