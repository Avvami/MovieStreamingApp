package com.bignerdranch.android.moviestreamingapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bignerdranch.android.moviestreamingapp.screens.adapters.ViewPagerAdapter
import com.bignerdranch.android.moviestreamingapp.screens.fragments.FavoriteFragment
import com.bignerdranch.android.moviestreamingapp.screens.fragments.MoviesFragment
import com.bignerdranch.android.moviestreamingapp.screens.fragments.SeriesFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbref = FirebaseDatabase.getInstance().reference
        dbref.child("sample").child("img").get().addOnSuccessListener {
            val imageRef = it.child("image").value.toString()
            Log.i("Link", imageRef)
            Picasso.get().load(imageRef).into(previewImage)
        }

        avatarImage.setOnClickListener() {
            startActivity(Intent(this@MainActivity, ProfileNMoreActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        //setUpTabs()
    }

    /*private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(MoviesFragment(), "ФИЛЬМЫ")
        adapter.addFragment(SeriesFragment(), "СЕРИАЛЫ")
        adapter.addFragment(FavoriteFragment(), "МОЙ СПИСОК")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }*/
}