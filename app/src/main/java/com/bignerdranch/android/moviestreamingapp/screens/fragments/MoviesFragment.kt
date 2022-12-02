package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.FragmentMoviesBinding
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bignerdranch.android.moviestreamingapp.screens.adapters.GridRecyclerAdapter
import com.google.firebase.database.*

class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var dbRef: DatabaseReference
    private var gridRecycler: RecyclerView? = null
    private var gridRecyclerAdapter: GridRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.trans_anim)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMoviesBinding.inflate(layoutInflater)

        dbRef = FirebaseDatabase.getInstance().reference
        val dbDBRef = dbRef.child("DB")

        dbDBRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val itemList: MutableList<CategoryItem> = ArrayList()

                    snapshot.children.forEach { foreach ->
                        if (foreach.child("movie").value == true) {
                            val title = foreach.key.toString()
                            val imageUrl = foreach.child("poster_min").value.toString()
                            itemList.add(CategoryItem(title, imageUrl))
                        } else {
                            //Do nothing
                        }
                    }

                    setGridRecycler(itemList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }
        })

        binding.moviesGroupBack.setOnClickListener{
            fragmentManager?.popBackStack()
        }

        return binding.root
    }

    private fun setGridRecycler(item: List<CategoryItem>) {
        gridRecycler = binding.moviesRV
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)
        gridRecycler!!.layoutManager = layoutManager
        gridRecyclerAdapter = context?.let { GridRecyclerAdapter(it, item) }
        gridRecycler!!.adapter = gridRecyclerAdapter

        gridRecyclerAdapter?.onItemClickListener(object : GridRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle = Bundle()
                bundle.putString("title", item[position].itemName)
                bundle.putString("preview_image", item[position].imageUrl)
                val fragment = DetailsFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.add(R.id.frameLayout, fragment)?.addToBackStack(null)?.commit()
            }

        })

    }
}