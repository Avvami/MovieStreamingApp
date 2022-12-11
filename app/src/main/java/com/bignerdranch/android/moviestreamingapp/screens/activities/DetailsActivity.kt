package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.ActivityDetailsBinding
import com.bignerdranch.android.moviestreamingapp.model.Availability
import com.bignerdranch.android.moviestreamingapp.screens.adapters.GridAvailableRecyclerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.muddz.styleabletoast.StyleableToast
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var title: String
    private lateinit var imageUrl: String
    private var gridRecycler: RecyclerView? = null
    private var gridRecyclerAdapter: GridAvailableRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().reference

        title = intent.getStringExtra("title").toString()
        imageUrl = intent.getStringExtra("preview_image").toString()

        binding.resourceTV.text = title
        Glide.with(this@DetailsActivity).load(imageUrl).apply(RequestOptions().dontTransform()).into(binding.posterImage)

        getDataFromFirebase(object : Callback {
            override fun onCallback(title: String, userFavourite: String, trailerLink: String) {

                //String to mutableList
                var userFavouriteTemp = userFavourite
                val previewIsChecked: Boolean
                if (userFavouriteTemp.endsWith(";"))
                    userFavouriteTemp = userFavouriteTemp.substring(0, userFavouriteTemp.length - 1)
                val userFavouriteList = userFavouriteTemp.split(";").map {  it.trim() }.toMutableList()

                //Check if banner exists
                previewIsChecked = if (userFavouriteList.contains(title)) {
                    binding.myListImage.setImageResource(R.drawable.icon_approve)
                    true
                } else {
                    binding.myListImage.setImageResource(R.drawable.icon_plus)
                    false
                }

                binding.myListGroup.setOnClickListener {
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

                binding.trailerBtn.setOnClickListener {
                    if (trailerLink.isNotEmpty()) {
                        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(trailerLink))
                        startActivity(intentBrowser)
                    } else {
                        StyleableToast.makeText(this@DetailsActivity, "Трейлер не доступен", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show()
                    }
                }
            }

        })

        binding.resourceGroupBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    interface Callback {
        fun onCallback(title: String, userFavourite: String, trailerLink: String)
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
                    Glide.with(applicationContext).load(itemRef.child("poster").value).apply(RequestOptions.bitmapTransform(multiTransformation)).into(binding.posterBlurImage)
                    binding.yearTV.text = itemRef.child("year").value.toString()
                    binding.ageTV.text = itemRef.child("age").value.toString()
                    binding.durationTV.text = itemRef.child("duration").value.toString()
                    binding.descriptionTV.text = itemRef.child("synopsis").value.toString()
                    binding.castTV.text = itemRef.child("cast").value.toString()
                    binding.genresTV.text = itemRef.child("genre").value.toString()
                    if (itemRef.child("movie").value == false) {
                        binding.about.text = "О сериале:"
                    } else binding.about.text = "О фильме:"
                    binding.aboutTV.text = itemRef.child("about").value.toString()

                    val itemList: MutableList<Availability> = ArrayList()
                    itemRef.child("available").children.forEach { foreach ->
                        when (foreach.key.toString().lowercase()) {
                            "Netflix".lowercase() -> itemList.add(Availability(foreach.value.toString(), R.drawable.netflix))
                            "Kinopoisk".lowercase() -> itemList.add(Availability(foreach.value.toString(), R.drawable.kinopoisk))
                            "HBO".lowercase() -> itemList.add(Availability(foreach.value.toString(), R.drawable.hbo))
                            "Hulu".lowercase() -> itemList.add(Availability(foreach.value.toString(), R.drawable.hulu))
                            else -> {
                                Log.d("LINK", "Something went wrong")
                            }
                        }
                    }
                    setGridRecycler(itemList)

                    val trailerLink: String = if (itemRef.child("trailer").exists())
                        itemRef.child("trailer").value.toString()
                    else ""

                    //User
                    val myListRef = currentUserRef.child("my_list")

                    val userFavourite: String = if (myListRef.exists()) {
                        myListRef.value.toString()
                    } else {
                        ""
                    }

                    finalCallback.onCallback(title, userFavourite, trailerLink)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }

        })
    }

    private fun setGridRecycler(item: List<Availability>) {
        gridRecycler = binding.availableRV
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 4)
        gridRecycler!!.layoutManager = layoutManager
        gridRecyclerAdapter = GridAvailableRecyclerAdapter(this, item)
        gridRecycler!!.adapter = gridRecyclerAdapter

        gridRecyclerAdapter?.onItemClickListener(object : GridAvailableRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (item[position].link.isNotEmpty()) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item[position].link))
                    startActivity(browserIntent)
                } else {
                    Log.d("TAG", "Wrong link")
                }
            }

        })
    }
}