package com.bignerdranch.android.moviestreamingapp.screens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.screens.fragments.HomeFragment
import com.bignerdranch.android.moviestreamingapp.screens.fragments.MoreFragment
import com.bignerdranch.android.moviestreamingapp.screens.fragments.SearchFragment
import com.bignerdranch.android.moviestreamingapp.screens.fragments.SoonFragment
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var soonFragment: SoonFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var moreFragment: MoreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFragment = HomeFragment()
        soonFragment = SoonFragment()
        searchFragment = SearchFragment()
        moreFragment = MoreFragment()

        replaceFragment(homeFragment)

        bottomNavigationView?.setOnItemSelectedListener() {

            when (it.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.soon -> replaceFragment(soonFragment)
                R.id.search -> replaceFragment(searchFragment)
                R.id.more -> replaceFragment(moreFragment)

                else -> {
                    StyleableToast.makeText(
                    this@MainActivity,
                    "Что-то пошло не так",
                    Toast.LENGTH_SHORT,
                        R.style.CustomToastStyle
                ).show()}
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