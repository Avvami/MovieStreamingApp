package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.FragmentSoonBinding
import com.bignerdranch.android.moviestreamingapp.model.SoonItem
import com.bignerdranch.android.moviestreamingapp.screens.adapters.SoonRecyclerAdapter
import com.google.firebase.database.*

class SoonFragment : Fragment() {

    private lateinit var binding: FragmentSoonBinding
    private lateinit var dbRef: DatabaseReference
    private var soonRecycler: RecyclerView? = null
    private var soonRecyclerAdapter: SoonRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSoonBinding.inflate(layoutInflater)

        dbRef = FirebaseDatabase.getInstance().reference
        val dbSoonRef = dbRef.child("Soon")

        dbSoonRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val itemList: MutableList<SoonItem> = ArrayList()

                    snapshot.children.forEach{ foreach ->
                        val title = foreach.child("name").value.toString()
                        val imageUrl = foreach.child("preview_poster").value.toString()
                        val logoUrl = foreach.child("preview_logo").value.toString()
                        val month = foreach.child("month").value.toString()
                        val monthDate = foreach.child("month_date").value.toString()
                        val coming = foreach.child("coming").value.toString()
                        val description = foreach.child("synopsis").value.toString()
                        itemList.add(SoonItem(title, imageUrl, logoUrl, month, monthDate, coming, description))
                    }

                    setSoonRecycler(itemList)
                } else {
                    //Do nothing
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }

        })

        return binding.root
    }

    private fun setSoonRecycler(item: List<SoonItem>) {
        soonRecycler = binding.soonRV
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        soonRecycler!!.layoutManager = layoutManager
        soonRecyclerAdapter = context?.let { SoonRecyclerAdapter(it, item) }
        soonRecycler!!.adapter = soonRecyclerAdapter

        soonRecyclerAdapter?.onItemClickListener(object : SoonRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val bundle = Bundle()
                bundle.putString("title", item[position].itemName)
                val fragment = DetailsFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.add(R.id.frameLayout, fragment)?.addToBackStack(null)?.commit()
            }

        })
    }
}