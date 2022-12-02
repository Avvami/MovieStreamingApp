package com.bignerdranch.android.moviestreamingapp.screens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.ActivityMainBinding
import com.bignerdranch.android.moviestreamingapp.screens.fragments.HomeFragment
import com.bignerdranch.android.moviestreamingapp.screens.fragments.MoreFragment
import com.bignerdranch.android.moviestreamingapp.screens.fragments.SearchFragment
import com.bignerdranch.android.moviestreamingapp.screens.fragments.SoonFragment
import io.github.muddz.styleabletoast.StyleableToast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding/*
    private lateinit var homeFragment: HomeFragment
    private lateinit var soonFragment: SoonFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var moreFragment: MoreFragment*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*homeFragment = HomeFragment()
        soonFragment = SoonFragment()
        searchFragment = SearchFragment()
        moreFragment = MoreFragment()*/

        replaceFragment(HomeFragment())

        binding.bottomNavigationView?.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.soon -> replaceFragment(SoonFragment())
                R.id.search -> replaceFragment(SearchFragment())
                R.id.more -> replaceFragment(MoreFragment())

                else -> {
                    StyleableToast.makeText(this@MainActivity, "Что-то пошло не так", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show()}
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}