package com.bignerdranch.android.moviestreamingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_resource_detail.*

class ResourceDetailActivity : AppCompatActivity() {

    private lateinit var resourceTitle: TextView
    private lateinit var resourceYear: TextView
    private lateinit var resourceAge: TextView
    private lateinit var resourceDuration: TextView
    private lateinit var resourceDescription: TextView
    private lateinit var resourceGenre: TextView
    private lateinit var resourcePoster: ShapeableImageView
    private lateinit var resourceMainPoster: ImageView
    private lateinit var resourceAboutText: TextView
    private lateinit var resourceAbout: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource_detail)

        resourceTitle = findViewById(R.id.resource_detail_name)
        resourceYear = findViewById(R.id.resource_detail_year)
        resourceAge = findViewById(R.id.resource_detail_age)
        resourceDuration = findViewById(R.id.resource_detail_duration)
        resourceDescription = findViewById(R.id.resource_detail_description)
        resourceGenre = findViewById(R.id.resource_detail_genre)
        resourcePoster = findViewById(R.id.resource_detail_poster)
        resourceMainPoster = findViewById(R.id.resource_main_poster)
        resourceAboutText = findViewById(R.id.about_text)
        resourceAbout = findViewById(R.id.resource_detail_about)

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
        resourceYear.text = intent.getStringExtra("resource_year")
        resourceAge.text = intent.getStringExtra("resource_age")
        resourceDuration.text = intent.getStringExtra("resource_duration")
        resourceDescription.text = intent.getStringExtra("resource_description")
        resourceGenre.text = intent.getStringExtra("resource_genre")
        Picasso.get().load(intent.getStringExtra("resource_poster")).into(resourcePoster)
        Picasso.get().load(intent.getStringExtra("resource_main_poster")).into(resourceMainPoster)
        resourceAbout.text = intent.getStringExtra("resource_about")

        if (intent.getStringExtra("resource_about_text") == "false") {
            resourceAboutText.text = "О сериале:"
        } else {
            resourceAboutText.text = "О фильме:"
        }
    }
}