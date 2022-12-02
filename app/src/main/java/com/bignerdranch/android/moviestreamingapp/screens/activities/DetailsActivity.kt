package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bignerdranch.android.moviestreamingapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.muddz.styleabletoast.StyleableToast
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var title: String
    private lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        dbRef = FirebaseDatabase.getInstance().reference

        title = intent.getStringExtra("title").toString()
        imageUrl = intent.getStringExtra("preview_image").toString()

        resourceTV.text = title
        Glide.with(this@DetailsActivity).load(imageUrl).apply(RequestOptions().dontTransform()).into(posterImage)

        getDataFromFirebase(object : Callback {
            override fun onCallback(title: String, userFavourite: String) {

                //String to mutableList
                var userFavouriteTemp = userFavourite
                val previewIsChecked: Boolean
                if (userFavouriteTemp.endsWith(";"))
                    userFavouriteTemp = userFavouriteTemp.substring(0, userFavouriteTemp.length - 1)
                val userFavouriteList = userFavouriteTemp.split(";").map {  it.trim() }.toMutableList()

                //Check if banner exists
                previewIsChecked = if (userFavouriteList.contains(title)) {
                    myListImage.setImageResource(R.drawable.icon_approve)
                    true
                } else {
                    myListImage.setImageResource(R.drawable.icon_plus)
                    false
                }

                myListGroup.setOnClickListener {
                    val dbUserRef = dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid!!)
                    val updatedUserFavorite: String

                    if (previewIsChecked) {
                        userFavouriteList.remove(title)
                        updatedUserFavorite = userFavouriteList.joinToString(";")
                        dbUserRef.child("my_list").setValue(updatedUserFavorite)

                        StyleableToast.makeText(this@DetailsActivity, "Удалено из моего списка", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show()
                    } else {
                        userFavouriteList.add(title)
                        updatedUserFavorite = userFavouriteList.joinToString(";")
                        dbUserRef.child("my_list").setValue(updatedUserFavorite)

                        StyleableToast.makeText(this@DetailsActivity, "Добавлено в мой список", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show()
                    }
                }
            }

        })

        resourceGroupBack.setOnClickListener {
            onBackPressed()
        }
    }

    interface Callback {
        fun onCallback(title: String, userFavourite: String)
    }

    private fun getDataFromFirebase (finalCallback: Callback) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dbDBRef = snapshot.child("DB")
                    val itemRef = dbDBRef.child(title)
                    val currentUserRef = snapshot.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid!!)

                    //Filling with content
                    val multiTransformation = MultiTransformation(
                        BlurTransformation(50, 3), ColorFilterTransformation(Color.argb(50, 0, 0, 0))
                    )
                    Glide.with(applicationContext).load(itemRef.child("poster").value).apply(RequestOptions.bitmapTransform(multiTransformation)).into(posterBlurImage)
                    yearTV.text = itemRef.child("year").value.toString()
                    ageTV.text = itemRef.child("age").value.toString()
                    durationTV.text = itemRef.child("duration").value.toString()
                    descriptionTV.text = itemRef.child("synopsis").value.toString()
                    castTV.text = itemRef.child("cast").value.toString()
                    genresTV.text = itemRef.child("genre").value.toString()
                    if (itemRef.child("movie").value == false) {
                        about.text = "О сериале:"
                    } else about.text = "О фильме:"
                    aboutTV.text = itemRef.child("about").value.toString()

                    //User
                    val myListRef = currentUserRef.child("my_list")

                    val userFavourite: String = if (myListRef.exists()) {
                        myListRef.value.toString()
                    } else {
                        ""
                    }

                    finalCallback.onCallback(title, userFavourite)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }

        })
    }
}