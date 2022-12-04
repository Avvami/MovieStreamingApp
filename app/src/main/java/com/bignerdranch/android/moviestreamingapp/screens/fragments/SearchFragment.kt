package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.FragmentSearchBinding
import com.bignerdranch.android.moviestreamingapp.extentions.hideKeyboard

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        fragmentManager?.beginTransaction()?.replace(R.id.searchFrameL, SearchHistoryFragment())?.commit()

        binding.searchET.addTextChangedListener {
            if (binding.searchET.text.toString().isEmpty()) {
                fragmentManager?.beginTransaction()?.replace(R.id.searchFrameL, SearchHistoryFragment())?.commit()
            } else {
                val bundle = Bundle()
                val query = binding.searchET.text.toString()
                bundle.putString("query", query)
                val fragment = SearchResultFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.searchFrameL, fragment)?.commit()
            }
        }

        binding.searchBtn.setOnClickListener {
            activity?.hideKeyboard()
        }

        binding.root.setOnClickListener {
            activity?.hideKeyboard()
        }

        return binding.root
    }
}