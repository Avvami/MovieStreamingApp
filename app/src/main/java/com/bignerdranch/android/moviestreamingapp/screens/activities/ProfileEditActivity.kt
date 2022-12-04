package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.ActivityProfileEditBinding
import com.bignerdranch.android.moviestreamingapp.extentions.hideKeyboard
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.muddz.styleabletoast.StyleableToast

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().reference
        val userRef = dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid!!)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val profileImageRef = snapshot.child("profile_image")
                    if (profileImageRef.exists()) {
                        Glide.with(applicationContext).load(profileImageRef.value).into(binding.profileImageEdit)
                    } else {
                        //No profile image
                    }
                    binding.profileUsernameET.setText(snapshot.child("username").value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Do nothing
            }

        })

        binding.profileImageEdit.setOnClickListener {
            startActivity(Intent(this@ProfileEditActivity, ChangeProfileIconActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        binding.applyBtn.setOnClickListener {
            hideKeyboard()
            val newUsername = binding.profileUsernameET.text.toString()
            if (newUsername.isEmpty()) {
                StyleableToast.makeText(
                    this@ProfileEditActivity,
                    "Имя пользователя должно содержать хотя бы один символ",
                    Toast.LENGTH_SHORT,
                    R.style.CustomToastStyle
                ).show()
            } else {
                userRef.child("username").setValue(newUsername)
            }
        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }

        binding.profileEditGroupBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}