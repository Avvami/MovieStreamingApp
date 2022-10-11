package com.bignerdranch.android.moviestreamingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.moviestreamingapp.screens.adapters.ViewPagerAdapter
import com.bignerdranch.android.moviestreamingapp.screens.fragments.FavoriteFragment
import com.bignerdranch.android.moviestreamingapp.screens.fragments.MoviesFragment
import com.bignerdranch.android.moviestreamingapp.screens.fragments.SeriesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpTabs()
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(MoviesFragment(), "ФИЛЬМЫ")
        adapter.addFragment(SeriesFragment(), "СЕРИАЛЫ")
        adapter.addFragment(FavoriteFragment(), "МОЙ СПИСОК")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    /*override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this@MainActivity, StartupScreenActivity::class.java))
        }
    }*/
}