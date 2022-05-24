package com.bignerdranch.android.moviestreamingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton

class ResourceDetailActivity : AppCompatActivity() {

    private lateinit var resourceTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource_detail)

        resourceTitle = findViewById(R.id.resource_detail_name)

        val favoriteToggle = findViewById<ToggleButton>(R.id.favorite_toggle)

        favoriteToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Is on", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Is off", Toast.LENGTH_SHORT).show()
            }
        }

        setValuesToView()
    }

    private fun setValuesToView() {

        resourceTitle.text = intent.getStringExtra("resource_name")
    }
}