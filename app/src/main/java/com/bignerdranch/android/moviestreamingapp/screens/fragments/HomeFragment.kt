package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.FragmentHomeBinding
import com.bignerdranch.android.moviestreamingapp.model.AllCategory
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bignerdranch.android.moviestreamingapp.screens.activities.MainActivity
import com.bignerdranch.android.moviestreamingapp.screens.activities.ProfileNMoreActivity
import com.bignerdranch.android.moviestreamingapp.screens.adapters.MainRecyclerAdapter
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.muddz.styleabletoast.StyleableToast

class HomeFragment : Fragment() {

    private lateinit var dbRef : DatabaseReference
    private lateinit var binding: FragmentHomeBinding
    private var mainCategoryRecycler : RecyclerView? = null
    private var mainRecyclerAdapter : MainRecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)

        dbRef = FirebaseDatabase.getInstance().reference

        val currentUser = dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid!!)
        currentUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val imgRef = snapshot.child("profile_image").value.toString()
                    if (imgRef.isEmpty()) {
                        //Do nothing
                    } else {
                        Glide.with(this@HomeFragment).load(imgRef).into(binding.profileImage)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Do nothing
            }

        })

        val dbDBRef = dbRef.child("DB")
        val dbCategoriesRef = dbRef.child("Categories")
        var preview = ""
        val homePreview = dbRef.child("Preview")

        homePreview.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    preview = snapshot.child("Home").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }

        })

        val allCategory: MutableList<AllCategory> = ArrayList()
        dbCategoriesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //if (snapshot.exists()) {
                    snapshot.children.forEach { foreach ->
                        val categoryTitle = foreach.key.toString()
                        val categoryItem = foreach.value.toString()
                        val itemList: List<String> = categoryItem.split(";").map { its -> its.trim() }
                        val categoryItemList: MutableList<CategoryItem> = ArrayList()
                        itemList.forEach { value ->
                            val title = dbDBRef.child(value).key
                            dbDBRef.child(value).child("poster_min").get().addOnSuccessListener { get ->
                                //binding.textView3.text = "${title}"
                                categoryItemList.add(CategoryItem("$title", "${get.value}"))
                            }
                        }
                        categoryItemList.forEach{
                            binding.textView3.text = it.imageUrl
                        }
                        allCategory.add(AllCategory(categoryTitle, categoryItemList))
                        setMainCategoryRecycler(allCategory)
                    }
                //}
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }

        })
        /*dbCategoriesRef.get().addOnSuccessListener {
            val category = it.children.toString()
            val categoryItem = it.value.toString()
            binding.textView3.text = category
            val categoryItemList: MutableList<CategoryItem> = ArrayList()
        }*/
        dbRef.child("DB").get().addOnSuccessListener {

            val imgRef = it.child(preview).child("poster").value.toString()
            Glide.with(this@HomeFragment).load(imgRef).into(binding.previewImage)
        }

        val categoryItemList1: MutableList<CategoryItem> = ArrayList()
        categoryItemList1.add(CategoryItem("Some1", "https://firebasestorage.googleapis.com/v0/b/coursework-9ccdd.appspot.com/o/movies-small%2Fawake.jpg?alt=media&token=cd2e66e3-cc3f-4dd6-8345-1d33ed3dd677"))
        categoryItemList1.add(CategoryItem("Some2", "https://firebasestorage.googleapis.com/v0/b/coursework-9ccdd.appspot.com/o/movies-small%2Fdevil%20all%20the%20time.jpg?alt=media&token=5959614e-8024-4ec7-b688-c870f59daf14"))
        categoryItemList1.add(CategoryItem("Some3", "https://firebasestorage.googleapis.com/v0/b/coursework-9ccdd.appspot.com/o/movies-small%2Fgreen%20book.jpg?alt=media&token=c254c222-7581-44ee-a944-3c56986563bb"))
        categoryItemList1.add(CategoryItem("Some4", "https://firebasestorage.googleapis.com/v0/b/coursework-9ccdd.appspot.com/o/movies-small%2Fin%20the%20shadow%20of%20the%20moon.jpg?alt=media&token=7a617600-099c-4d68-9540-3b21025e2b0d"))
        categoryItemList1.add(CategoryItem("Some5", "https://firebasestorage.googleapis.com/v0/b/coursework-9ccdd.appspot.com/o/movies-small%2Fonce%20upon%20a%20time%20in%20hollywood.jpg?alt=media&token=d95bfa70-8ced-4703-93a7-068afcc59e6d"))

        val categoryItemList2: MutableList<CategoryItem> = ArrayList()
        categoryItemList2.add(CategoryItem("Some6", "https://firebasestorage.googleapis.com/v0/b/coursework-9ccdd.appspot.com/o/series-small%2Falice%20in%20borderlands.jpg?alt=media&token=c21cdc24-3af9-4736-a0b0-515ca3d9775b"))
        categoryItemList2.add(CategoryItem("Some7", "https://firebasestorage.googleapis.com/v0/b/coursework-9ccdd.appspot.com/o/series-small%2Fgame%20of%20thrones.jpg?alt=media&token=6a692c4b-3d02-4a24-8a66-f6f1c0f7d0ad"))
        categoryItemList2.add(CategoryItem("Some8", "https://firebasestorage.googleapis.com/v0/b/coursework-9ccdd.appspot.com/o/series-small%2Fhow%20to%20sell%20drugs%20online.jpg?alt=media&token=e3d4de81-c26e-43a2-abff-8a3f03055efa"))
        categoryItemList2.add(CategoryItem("Some9", "https://firebasestorage.googleapis.com/v0/b/coursework-9ccdd.appspot.com/o/series-small%2Fbojack%20horseman.jpg?alt=media&token=0c762645-fde1-47a0-98bc-2edddf4be0a4"))
        categoryItemList2.add(CategoryItem("Some10", "https://firebasestorage.googleapis.com/v0/b/coursework-9ccdd.appspot.com/o/series-small%2Flupin.jpg?alt=media&token=1359ec17-9a8e-4951-8814-2670953a9f0f"))

        val allCategory2: MutableList<AllCategory> = ArrayList()
        allCategory2.add(AllCategory("Socks in Sandals", categoryItemList1))
        allCategory2.add(AllCategory("Crap and Paradise", categoryItemList2))
        setMainCategoryRecycler(allCategory2)

        binding.profileImage.setOnClickListener() {
            val intent = Intent(activity, ProfileNMoreActivity::class.java)
            activity?.startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        return binding.root
    }

    private fun setMainCategoryRecycler (allCategory: List<AllCategory>) {

        mainCategoryRecycler = binding.categoryRV
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        mainCategoryRecycler!!.layoutManager = layoutManager
        mainRecyclerAdapter = context?.let { MainRecyclerAdapter(it, allCategory) }
        mainCategoryRecycler!!.adapter = mainRecyclerAdapter
    }
}