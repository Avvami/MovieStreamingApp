package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.data.Resource
import com.bignerdranch.android.moviestreamingapp.screens.adapters.RecyclerViewAdapter
import com.google.firebase.database.*

class SeriesFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var resourceRecyclerView : RecyclerView
    private lateinit var resourceArrayList : ArrayList<Resource>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_series, container, false)

        resourceRecyclerView = view.findViewById(R.id.RVLayout_s)
        resourceRecyclerView.layoutManager = LinearLayoutManager(context)
        resourceRecyclerView.setHasFixedSize(true)

        resourceArrayList = arrayListOf<Resource>()
        getResourceData()

        return view
    }

    private fun getResourceData() {

        dbref = FirebaseDatabase.getInstance().getReference("Series")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (resourceSnapshot in snapshot.children) {

                        val resource = resourceSnapshot.getValue(Resource::class.java)
                        resourceArrayList.add(resource!!)
                    }
                    resourceRecyclerView.adapter = RecyclerViewAdapter(resourceArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}