package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.ActivityProfileNmoreBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileNMoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileNmoreBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileNmoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().reference
        val userRef = dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid!!)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val profileImageRef = snapshot.child("profile_image")
                    if (profileImageRef.exists()) {
                        Glide.with(applicationContext).load(profileImageRef.value).into(binding.profileImageStroke)
                    } else {
                        //No profile image
                    }
                    binding.usernameTV.text = snapshot.child("username").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Do nothing
            }

        })

        binding.profileNMoreGroupBack.setOnClickListener {
            onBackPressed()
        }

        binding.signOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@ProfileNMoreActivity, StartupScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.editProfileGroup.setOnClickListener {
            startActivity(Intent(this@ProfileNMoreActivity, ProfileEditActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        binding.notificationGroup.setOnClickListener {
            startActivity(Intent(this@ProfileNMoreActivity, NotificationsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        binding.myListGroup.setOnClickListener {
            startActivity(Intent(this@ProfileNMoreActivity, MyListActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        binding.accountGroup.setOnClickListener {
            startActivity(Intent(this@ProfileNMoreActivity, AccountSettingActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        binding.aboutGroup.setOnClickListener {
            startActivity(Intent(this@ProfileNMoreActivity, AboutAppActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}