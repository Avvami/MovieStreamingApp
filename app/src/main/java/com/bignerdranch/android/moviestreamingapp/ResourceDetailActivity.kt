package com.bignerdranch.android.moviestreamingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class ResourceDetailActivity : AppCompatActivity() {

    private lateinit var resourceTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource_detail)

        resourceTitle = findViewById(R.id.resource_detail_name)
        setValuesToView()
    }

    private fun setValuesToView() {

        resourceTitle.text = intent.getStringExtra("resource_name")
    }
}