package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bignerdranch.android.moviestreamingapp.R
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.activity_profile_edit.*

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        dbRef = FirebaseDatabase.getInstance().reference
        val userRef = dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid!!)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val profileImageRef = snapshot.child("profile_image")
                    if (profileImageRef.exists()) {
                        Glide.with(applicationContext).load(profileImageRef.value).into(profileImageEdit)
                    } else {
                        //No profile image
                    }
                    profileUsernameET.setText(snapshot.child("username").value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Do nothing
            }

        })

        profileImageEdit.setOnClickListener {
            startActivity(Intent(this@ProfileEditActivity, ChangeProfileIconActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        applyBtn.setOnClickListener {
            val newUsername = profileUsernameET.text.toString()
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

        profileEditGroupBack.setOnClickListener() {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}