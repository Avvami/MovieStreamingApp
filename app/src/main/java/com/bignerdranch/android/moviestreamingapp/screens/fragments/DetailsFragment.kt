package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.FragmentDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.muddz.styleabletoast.StyleableToast
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation

class DetailsFragment : Fragment() {

    private lateinit var binding : FragmentDetailsBinding
    private lateinit var dbRef : DatabaseReference
    private lateinit var title: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailsBinding.inflate(layoutInflater)

        dbRef = FirebaseDatabase.getInstance().reference
        title = arguments?.getString("title").toString()

        binding.resourceTV.text = title
        Glide.with(this@DetailsFragment).load(arguments?.getString("preview_image")).into(binding.posterImage)

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

                        context?.let { it1 -> StyleableToast.makeText(it1, "Удалено из моего списка", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show() }
                    } else {
                        userFavouriteList.add(title)
                        updatedUserFavorite = userFavouriteList.joinToString(";")
                        dbUserRef.child("my_list").setValue(updatedUserFavorite)

                        context?.let { it1 -> StyleableToast.makeText(it1, "Добавлено в мой список", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show() }
                    }
                }
            }

        })
        binding.resourceGroupBack.setOnClickListener{
            fragmentManager?.popBackStack()
        }

        return binding.root
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
                    context?.let { Glide.with(it).load(itemRef.child("poster").value).apply(RequestOptions.bitmapTransform(multiTransformation)).into(binding.posterBlurImage) }
                    binding.yearTV.text = itemRef.child("year").value.toString()
                    binding.ageTV.text = itemRef.child("age").value.toString()
                    binding.durationTV.text = itemRef.child("duration").value.toString()
                    binding.descriptionTV.text = itemRef.child("synopsis").value.toString()
                    binding.castTV.text = itemRef.child("cast").value.toString()
                    binding.genresTV.text = itemRef.child("genre").value.toString()
                    if (itemRef.child("movie").value == false)
                        binding.about.text = "О сериале:"
                    else binding.about.text = "О фильме:"
                    binding.aboutTV.text = itemRef.child("about").value.toString()

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