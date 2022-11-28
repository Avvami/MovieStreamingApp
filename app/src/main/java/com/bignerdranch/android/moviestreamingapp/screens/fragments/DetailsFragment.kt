package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.FragmentDetailsBinding
import com.bumptech.glide.Glide

class DetailsFragment : Fragment() {

    private lateinit var binding : FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)

        binding.resourceTV.text = arguments?.getString("title")
        Glide.with(this@DetailsFragment).load(arguments?.getString("preview_image")).into(binding.posterImage)

        binding.resourceGroupBack.setOnClickListener{
            fragmentManager?.popBackStack()
        }

        return binding.root
    }
}