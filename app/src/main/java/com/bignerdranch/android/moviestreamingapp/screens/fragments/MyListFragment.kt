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
import com.bignerdranch.android.moviestreamingapp.databinding.FragmentMyListBinding
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bignerdranch.android.moviestreamingapp.screens.adapters.GridRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyListFragment : Fragment() {

    private lateinit var binding: FragmentMyListBinding
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

        binding = FragmentMyListBinding.inflate(layoutInflater)

        dbRef = FirebaseDatabase.getInstance().reference

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userRef = snapshot.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                    val dbDBRef = snapshot.child("DB")

                    val itemList: MutableList<CategoryItem> = ArrayList()
                    var myListString = userRef.child("my_list").value.toString()
                    if (myListString.endsWith(";"))
                        myListString = myListString.substring(0, myListString.length - 1)
                    if (myListString.startsWith(";"))
                        myListString= myListString.substring(1, myListString.length)
                    val itemListTemp = myListString.split(";").map { its -> its.trim() }.toMutableList()

                    itemListTemp.forEach { value ->
                        if (value == dbDBRef.child(value).key) {
                            val title = dbDBRef.child(value).key.toString()
                            val imageUrl = dbDBRef.child(value).child("poster_min").value.toString()
                            itemList.add(CategoryItem(title, imageUrl))
                        }
                    }

                    setGridRecycler(itemList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }
        })

        binding.myListGroupBack.setOnClickListener{
            fragmentManager?.popBackStack()
        }

        return binding.root
    }

    private fun setGridRecycler(item: List<CategoryItem>) {
        gridRecycler = binding.myListRV
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