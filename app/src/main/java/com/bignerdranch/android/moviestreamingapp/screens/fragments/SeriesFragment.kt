package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.ResourceDetailActivity
import com.bignerdranch.android.moviestreamingapp.data.Resource
import com.bignerdranch.android.moviestreamingapp.data.ResourceAllDetails
import com.bignerdranch.android.moviestreamingapp.screens.adapters.RecyclerViewAdapter
import com.google.firebase.database.*

class SeriesFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var resourceRecyclerView : RecyclerView
    private lateinit var resourceArrayList : ArrayList<Resource>
    private lateinit var resourceArrayListAll : ArrayList<ResourceAllDetails>

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
        resourceArrayListAll = arrayListOf<ResourceAllDetails>()
        getResourceData()

        return view
    }

    private fun getResourceData() {

        dbref = FirebaseDatabase.getInstance().getReference("Series")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    resourceArrayList.clear()
                    resourceArrayListAll.clear()
                    for (resourceSnapshot in snapshot.children) {

                        val resource = resourceSnapshot.getValue(Resource::class.java)
                        val resourceAll = resourceSnapshot.getValue(ResourceAllDetails::class.java)
                        resourceArrayList.add(resource!!)
                        resourceArrayListAll.add(resourceAll!!)
                    }
                    val itAdapter = RecyclerViewAdapter(resourceArrayList)
                    resourceRecyclerView.adapter = itAdapter

                    itAdapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {

                            //Toast.makeText(activity, "you clicked on item no. $position", Toast.LENGTH_SHORT).show()
                            val intent = Intent(activity, ResourceDetailActivity::class.java)

                            intent.putExtra("resource_age", resourceArrayListAll[position].age)
                            intent.putExtra("resource_description", resourceArrayListAll[position].description)
                            intent.putExtra("resource_duration", resourceArrayListAll[position].duration)
                            intent.putExtra("resource_genre", resourceArrayListAll[position].genre)
                            intent.putExtra("resource_main_poster", resourceArrayListAll[position].main_poster)
                            intent.putExtra("resource_name", resourceArrayListAll[position].name)
                            intent.putExtra("resource_poster", resourceArrayListAll[position].poster)
                            intent.putExtra("resource_year", resourceArrayListAll[position].year)
                            intent.putExtra("resource_isMovie", resourceArrayListAll[position].movie)
                            intent.putExtra("resource_about", resourceArrayListAll[position].about)
                            intent.putExtra("resource_inList", resourceArrayListAll[position].favorite)
                            activity?.startActivity(intent)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Do nothing
            }
        })
    }
}