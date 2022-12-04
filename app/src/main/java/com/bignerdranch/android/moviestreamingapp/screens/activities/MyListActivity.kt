package com.bignerdranch.android.moviestreamingapp.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.ActivityMyListBinding
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bignerdranch.android.moviestreamingapp.screens.adapters.GridRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MyListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyListBinding
    private lateinit var dbRef: DatabaseReference
    private var gridRecycler: RecyclerView? = null
    private var gridRecyclerAdapter: GridRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.myListGroupBack.setOnClickListener() {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    private fun setGridRecycler(item: List<CategoryItem>) {
        gridRecycler = binding.myListRV
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this@MyListActivity, 3)
        gridRecycler!!.layoutManager = layoutManager
        gridRecyclerAdapter = GridRecyclerAdapter(this@MyListActivity, item)
        gridRecycler!!.adapter = gridRecyclerAdapter

        gridRecyclerAdapter?.onItemClickListener(object : GridRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MyListActivity, DetailsActivity::class.java)
                intent.putExtra("title", item[position].itemName)
                intent.putExtra("preview_image", item[position].imageUrl)
                startActivity(intent)
            }

        })

    }
}