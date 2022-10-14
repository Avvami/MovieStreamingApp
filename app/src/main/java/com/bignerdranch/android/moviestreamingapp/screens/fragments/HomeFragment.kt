package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.FragmentHomeBinding
import com.bignerdranch.android.moviestreamingapp.screens.activities.ProfileNMoreActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)

        dbref = FirebaseDatabase.getInstance().reference
        dbref.child("sample").child("img").get().addOnSuccessListener {
            val imageRef = it.child("image").value.toString()
            Log.i("Link", imageRef)
            Picasso.get().load(imageRef).into(binding.previewImage)
        }

        binding.avatarImage.setOnClickListener() {
            val intent = Intent(activity, ProfileNMoreActivity::class.java)
            activity?.startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        return binding.root
    }
}