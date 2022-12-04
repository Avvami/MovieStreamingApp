package com.bignerdranch.android.moviestreamingapp.screens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.model.AllCategory
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bignerdranch.android.moviestreamingapp.databinding.ActivityChangeProfileIconBinding
import com.bignerdranch.android.moviestreamingapp.screens.adapters.MainRecyclerIconAdapter
import com.google.firebase.database.*

class ChangeProfileIconActivity : AppCompatActivity() {

    private lateinit var dbRef : DatabaseReference
    private lateinit var binding: ActivityChangeProfileIconBinding
    private var mainIconCategoryRecycler : RecyclerView? = null
    private var mainIconRecyclerAdapter : MainRecyclerIconAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileIconBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().reference

        //Nested recyclerview
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allIconCategory: MutableList<AllCategory> = ArrayList()

                if (snapshot.exists()) {
                    val dbIconsRef = snapshot.child("ProfileImages")

                    dbIconsRef.children.forEach { foreach ->
                        val categoryIconsTitle = foreach.key.toString()
                        val categoryIconsList: MutableList<CategoryItem> = ArrayList()

                        dbIconsRef.child(categoryIconsTitle).children.forEach { value ->
                            val title = value.key.toString()
                            val iconUrl = value.value.toString()
                            categoryIconsList.add(CategoryItem(title, iconUrl))
                        }

                        allIconCategory.add(AllCategory(categoryIconsTitle, categoryIconsList))
                        setMainIconCategoryRecycler(allIconCategory)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }
        })

        binding.changeIconGroupBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setMainIconCategoryRecycler (allCategory: List<AllCategory>) {
        mainIconCategoryRecycler = binding.categoryIconsRV
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@ChangeProfileIconActivity)
        mainIconCategoryRecycler!!.layoutManager = layoutManager
        mainIconRecyclerAdapter = MainRecyclerIconAdapter(this@ChangeProfileIconActivity, allCategory)
        mainIconCategoryRecycler!!.adapter = mainIconRecyclerAdapter

    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }
}