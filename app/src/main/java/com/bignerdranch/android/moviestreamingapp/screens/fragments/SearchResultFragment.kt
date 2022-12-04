package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.FragmentSearchResultBinding
import com.bignerdranch.android.moviestreamingapp.extentions.hideKeyboard
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bignerdranch.android.moviestreamingapp.screens.adapters.GridRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var query: String
    private var gridRecycler: RecyclerView? = null
    private var gridRecyclerAdapter: GridRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(layoutInflater)

        query = arguments?.getString("query").toString()
        dbRef = FirebaseDatabase.getInstance().reference
        val dbDBRef = dbRef.child("DB")

        dbDBRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList: MutableList<CategoryItem> = ArrayList()

                snapshot.children.forEach { foreach ->
                    if (foreach.key?.lowercase()?.contains(query.lowercase()) == true) {
                        val title = foreach.key.toString()
                        val imageUrl = foreach.child("poster_min").value.toString()
                        itemList.add(CategoryItem(title, imageUrl))
                    }
                }

                setGridRecycler(itemList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }
        })

        return binding.root
    }

    private fun setGridRecycler(item: List<CategoryItem>) {
        gridRecycler = binding.searchResultRV
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)
        gridRecycler!!.layoutManager = layoutManager
        gridRecyclerAdapter = context?.let { GridRecyclerAdapter(it, item) }
        gridRecycler!!.adapter = gridRecyclerAdapter

        gridRecyclerAdapter?.onItemClickListener(object : GridRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val userRef = dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("search_history")

                userRef.addValueEventListener (object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var userHistory = snapshot.value.toString()
                            if (userHistory.endsWith(";"))
                                userHistory = userHistory.substring(0, userHistory.length - 1)
                            if (userHistory.startsWith(";"))
                                userHistory = userHistory.substring(1, userHistory.length)
                            val historyList = userHistory.split(";").map { it.trim() }.toMutableList()

                            if (!historyList.contains(item[position].itemName)) {
                                historyList.add(item[position].itemName)
                                val updatedHistory = historyList.joinToString(";")
                                Log.d("CHECKING", "third check")
                                dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("search_history").setValue(updatedHistory)
                            } else {
                                //Skip
                            }
                        } else {
                            dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("search_history").setValue(item[position].itemName)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("TAG", error.message)
                    }
                })

                /*getDataFromFirebase(object : Callback {
                    override fun onCallBack(userHistory: String) {

                        val dbUserRef = dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)

                        var userHistoryTemp = userHistory
                        if (userHistoryTemp.endsWith(";"))
                            userHistoryTemp = userHistoryTemp.substring(0, userHistoryTemp.length - 1)
                        val userHistoryList = userHistoryTemp.split(";").map { it.trim() }.toMutableList()

                        val updatedUserHistory: String
                        if (!userHistoryList.contains(item[position].itemName)) {
                            userHistoryList.add(item[position].itemName)
                            updatedUserHistory = userHistoryList.joinToString(";")
                            dbUserRef.child("search_history").setValue(updatedUserHistory)
                        } else {
                            //Do nothing
                        }
                    }

                })*/

                activity?.hideKeyboard()
                val bundle = Bundle()
                bundle.putString("title", item[position].itemName)
                bundle.putString("preview_image", item[position].imageUrl)
                val fragment = DetailsFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.add(R.id.frameLayout, fragment)?.addToBackStack(null)?.commit()
            }
        })

    }

    /*interface Callback {
        fun onCallBack(userHistory: String)
    }

    private fun getDataFromFirebase (finalCallback: Callback) {

        val userHistoryRef = dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)

        userHistoryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val userHistory: String = if (snapshot.child("search_history").exists()) {
                    snapshot.child("search_history").value.toString()
                } else {
                    ""
                }

                finalCallback.onCallBack(userHistory)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }

        })
    }*/
}