package com.bignerdranch.android.moviestreamingapp.screens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.moviestreamingapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile_edit.*

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        dbRef = FirebaseDatabase.getInstance().reference.child("Users")
        val currentUser = dbRef.child(FirebaseAuth.getInstance().currentUser?.uid!!)
        currentUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val imgRef = snapshot.child("profile_image").value.toString()
                    if (imgRef.isEmpty()) {
                        //Do nothing
                    } else {
                        Picasso.get().load(imgRef).into(profileImageEdit)
                    }
                    profileUsernameET.setText(snapshot.child("username").value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Do nothing
            }

        })

        profileEditGroupBack.setOnClickListener() {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}