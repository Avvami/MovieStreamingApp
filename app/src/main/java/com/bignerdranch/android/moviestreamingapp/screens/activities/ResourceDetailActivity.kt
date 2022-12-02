package com.bignerdranch.android.moviestreamingapp.screens.activities

import androidx.appcompat.app.AppCompatActivity

class ResourceDetailActivity : AppCompatActivity() {

    /*private var aboutR : String? = null
    private var ageR : String? = null
    private var descR : String? = null
    private var durR : String? = null
    private var favR : String? = null
    private var genreR : String? = null
    private var posterMR : String? = null
    private var movieR : String? = null
    private var nameR : String? = null
    private var posterR : String? = null
    private var yearR : String? = null
    private lateinit var dbMyList : DatabaseReference
    private lateinit var dbMovies : DatabaseReference
    private lateinit var dbSeries : DatabaseReference
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

        dbMyList = FirebaseDatabase.getInstance().getReference("MyList")
        dbMovies = FirebaseDatabase.getInstance().getReference("Movies")
        dbSeries = FirebaseDatabase.getInstance().getReference("Series")

        getValues()
        setValuesToView()

        favoriteToggle.isChecked = favR == "true"

        favoriteToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                favR = "true"
                val resourceData = ResourceAllDetails(aboutR, ageR, descR, durR, favR, genreR, posterMR, movieR, nameR, posterR, yearR)
                if (movieR == "true") {
                    dbMovies.child(nameR.toString()).child("favorite").setValue(favR)
                } else if (movieR == "false") {
                    dbSeries.child(nameR.toString()).child("favorite").setValue(favR)
                }
                dbMyList.child(nameR.toString()).setValue(resourceData)
            } else {
                favR = "false"
                if (movieR == "true") {
                    dbMovies.child(nameR.toString()).child("favorite").setValue(favR)
                } else if (movieR == "false") {
                    dbSeries.child(nameR.toString()).child("favorite").setValue(favR)
                }
                dbMyList.child(nameR.toString()).removeValue()
            }
        }
    }

    private fun setValuesToView() {

        resourceTitle.text = nameR
        resourceYear.text = yearR
        resourceAge.text = ageR
        resourceDuration.text = durR
        resourceDescription.text = descR
        resourceGenre.text = genreR
        Picasso.get().load(posterR).into(resourcePoster)
        Picasso.get().load(posterMR).into(resourceMainPoster)
        resourceAbout.text = aboutR

        if (movieR == "false") {
            resourceAboutText.text = "О сериале:"
        } else {
            resourceAboutText.text = "О фильме:"
        }
    }

    private fun getValues() {
        aboutR = intent.getStringExtra("resource_about")
        ageR = intent.getStringExtra("resource_age")
        descR = intent.getStringExtra("resource_description")
        durR = intent.getStringExtra("resource_duration")
        favR = intent.getStringExtra("resource_inList")
        genreR = intent.getStringExtra("resource_genre")
        posterMR = intent.getStringExtra("resource_main_poster")
        movieR = intent.getStringExtra("resource_isMovie")
        nameR = intent.getStringExtra("resource_name")
        posterR = intent.getStringExtra("resource_poster")
        yearR = intent.getStringExtra("resource_year")
    }*/
}