package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.FragmentSearchHistoryBinding
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bignerdranch.android.moviestreamingapp.screens.adapters.SearchHistoryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.custom_dialog.view.*

class SearchHistoryFragment : Fragment() {

    private lateinit var binding: FragmentSearchHistoryBinding
    private lateinit var dbRef: DatabaseReference
    private var title: String? = null
    private var searchHistory: String? = null
    private var searchHistoryRecycler: RecyclerView? = null
    private var searchHistoryRecyclerAdapter: SearchHistoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchHistoryBinding.inflate(layoutInflater)

        dbRef = FirebaseDatabase.getInstance().reference
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userSearchHistoryRef = snapshot.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("search_history")

                if (userSearchHistoryRef.exists()) {
                    if (userSearchHistoryRef.value.toString().isNotEmpty()) {
                        binding.searchHistoryRV.visibility = View.VISIBLE

                        val dbDBRef = snapshot.child("DB")
                        searchHistory = userSearchHistoryRef.value.toString()
                        if (searchHistory!!.endsWith(";"))
                            searchHistory = searchHistory!!.substring(0, searchHistory!!.length - 1)
                        if (searchHistory!!.startsWith(";"))
                            searchHistory = searchHistory!!.substring(1, searchHistory!!.length)
                        val itemList: List<String> = searchHistory!!.split(";").map { its -> its.trim() }
                        val searchHistoryList: MutableList<CategoryItem> = ArrayList()

                        Log.d("CHECKING", "first check")
                        itemList.forEach { foreach ->
                            val title = dbDBRef.child(foreach).key.toString()
                            val imageUrl = dbDBRef.child(foreach).child("poster_min").value.toString()
                            searchHistoryList.add(CategoryItem(title, imageUrl))
                        }
                        setSearchHistoryRecycler(searchHistoryList)
                    } else {
                        binding.searchHistoryRV.visibility = View.GONE
                        binding.clearHistoryTV.visibility = View.VISIBLE
                    }
                } else {
                    binding.searchHistoryRV.visibility = View.GONE
                    binding.clearHistoryTV.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }

        })

        return binding.root
    }

    private fun setSearchHistoryRecycler (item: MutableList<CategoryItem>) {
        searchHistoryRecycler = binding.searchHistoryRV
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        searchHistoryRecycler!!.layoutManager = layoutManager
        searchHistoryRecycler!!.setHasFixedSize(true)
        searchHistoryRecyclerAdapter = context?.let { SearchHistoryAdapter(it, item) }
        searchHistoryRecycler!!.adapter = searchHistoryRecyclerAdapter

        searchHistoryRecyclerAdapter?.onItemClickListener(object : SearchHistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle = Bundle()
                bundle.putString("preview_image", item[position].imageUrl)
                bundle.putString("title", item[position].itemName)
                val fragment = DetailsFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.add(R.id.frameLayout, fragment)?.addToBackStack(null)?.commit()
            }
        })

        searchHistoryRecyclerAdapter?.onItemLongClickListener(object : SearchHistoryAdapter.OnItemLongListener {
            override fun onItemLongClick(position: Int) {
                val dialogBinding = layoutInflater.inflate(R.layout.custom_dialog, null)
                val dialog = context?.let { Dialog(it) }
                dialog?.setContentView(dialogBinding)
                dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog?.setCancelable(true)
                title = item[position].itemName
                dialogBinding.titleName.text = title
                dialog?.show()

                dialogBinding.cancelBtn.setOnClickListener {
                    dialog?.dismiss()
                }

                dialogBinding.deleteBtn.setOnClickListener {

                    if (searchHistory!!.endsWith(";"))
                        searchHistory = searchHistory!!.substring(0, searchHistory!!.length - 1)
                    if (searchHistory!!.startsWith(";"))
                        searchHistory = searchHistory!!.substring(1, searchHistory!!.length)
                    val historyList = searchHistory!!.split(";").map { its -> its.trim() }.toMutableList()

                    historyList.remove(title)
                    searchHistory = historyList.joinToString(";")
                    Log.d("CHECKING", "second check")
                    dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("search_history").setValue(searchHistory)
                    dialog?.dismiss()
                }
            }

        })
    }
}