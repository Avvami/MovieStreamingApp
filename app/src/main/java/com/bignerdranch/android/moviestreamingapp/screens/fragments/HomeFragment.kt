package com.bignerdranch.android.moviestreamingapp.screens.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.databinding.FragmentHomeBinding
import com.bignerdranch.android.moviestreamingapp.model.AllCategory
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bignerdranch.android.moviestreamingapp.screens.activities.ProfileNMoreActivity
import com.bignerdranch.android.moviestreamingapp.screens.adapters.MainRecyclerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var dbRef : DatabaseReference
    private lateinit var binding: FragmentHomeBinding
    private var mainCategoryRecycler : RecyclerView? = null
    private var mainRecyclerAdapter : MainRecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater)

        dbRef = FirebaseDatabase.getInstance().reference

        getDataFromFirebase(object : Callback {
            override fun onCallBack(previewTitle: String, previewUrl: String, userFavourite: String) {

                binding.moreGroup.setOnClickListener {
                    Log.d("TAG", "wow")
                    val bundle = Bundle()
                    bundle.putString("title", previewTitle)
                    bundle.putString("preview_image", previewUrl)
                    val fragment = DetailsFragment()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)?.add(R.id.frameLayout, fragment)?.addToBackStack(null)?.commit()
                }

                //String to mutableList
                var userFavouriteTemp = userFavourite
                val previewIsChecked: Boolean
                if (userFavouriteTemp.endsWith(";"))
                    userFavouriteTemp = userFavouriteTemp.substring(0, userFavouriteTemp.length - 1)
                if (userFavouriteTemp.startsWith(";"))
                    userFavouriteTemp = userFavouriteTemp.substring(1, userFavouriteTemp.length)
                val userFavouriteList = userFavouriteTemp.split(";").map {  it.trim() }.toMutableList()

                //Check if banner exists
                previewIsChecked = if (userFavouriteList.contains(previewTitle)) {
                    binding.myListImage.setImageResource(R.drawable.icon_approve)
                    true
                } else {
                    binding.myListImage.setImageResource(R.drawable.icon_plus)
                    false
                }

                binding.myListGroup.setOnClickListener {
                    val dbUserRef = dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid!!)
                    val updatedUserFavorite: String

                    if (previewIsChecked) {
                        userFavouriteList.remove(previewTitle)
                        updatedUserFavorite = userFavouriteList.joinToString(";")
                        dbUserRef.child("my_list").setValue(updatedUserFavorite)

                        context?.let { it1 -> StyleableToast.makeText(it1, "Удалено из моего списка", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show() }
                    } else {
                        userFavouriteList.add(previewTitle)
                        updatedUserFavorite = userFavouriteList.joinToString(";")
                        dbUserRef.child("my_list").setValue(updatedUserFavorite)

                        context?.let { it1 -> StyleableToast.makeText(it1, "Добавлено в мой список", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show() }
                    }
                }
            }
        })

        binding.profileImage.setOnClickListener {
            val intent = Intent(activity, ProfileNMoreActivity::class.java)
            activity?.startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }

        binding.seriesRL.setOnClickListener {
            val fragment = SeriesFragment()
            fragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)?.add(R.id.frameLayout, fragment)?.addToBackStack(null)?.commit()
        }

        binding.moviesRL.setOnClickListener {
            val fragment = MoviesFragment()
            fragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)?.add(R.id.frameLayout, fragment)?.addToBackStack(null)?.commit()
        }

        binding.myListRL.setOnClickListener {
            val fragment = MyListFragment()
            fragmentManager?.beginTransaction()?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)?.add(R.id.frameLayout, fragment)?.addToBackStack(null)?.commit()
        }

        return binding.root
    }

    interface Callback {
        fun onCallBack(previewTitle: String, previewUrl: String, userFavourite: String)
    }

    private fun getDataFromFirebase (finalCallback: Callback) {

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allCategory: MutableList<AllCategory> = ArrayList()

                if (snapshot.exists()) {
                    val dbDBRef = snapshot.child("DB")
                    val dbCategoriesRef = snapshot.child("Categories")
                    val homePreview = snapshot.child("Preview")
                    val currentUserRef = snapshot.child("Users").child(FirebaseAuth.getInstance().currentUser?.uid!!)

                    //Preview banner
                    val previewTitle: String = homePreview.child("Home").value.toString()
                    val previewUrl: String = dbDBRef.child(previewTitle).child("poster_min").value.toString()
                    val previewLargeUrl = dbDBRef.child(previewTitle).child("poster").value.toString()
                    context?.let { Glide.with(it).load(previewLargeUrl).apply(RequestOptions().dontTransform()).into(binding.previewImage) }
                    binding.playBtn.setOnClickListener {
                        if (dbDBRef.child(previewTitle).child("trailer").exists()) {
                            val trailerLink = dbDBRef.child(previewTitle).child("trailer").value.toString()
                            val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(trailerLink))
                            startActivity(intentBrowser)
                        } else {
                            context?.let { it1 -> StyleableToast.makeText(it1, "Трейлер не доступен", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show() }
                        }
                    }

                    //User
                    val profileImageRef = currentUserRef.child("profile_image")
                    val myListRef = currentUserRef.child("my_list")

                    if (profileImageRef.exists()) {
                        context?.let { Glide.with(it).load(profileImageRef.value).into(binding.profileImage) }
                    } else {
                        //No profile image
                    }

                    val userFavourite: String = if (myListRef.exists()) {
                        myListRef.value.toString()
                    } else {
                        ""
                    }

                    //For nested recycler view
                    dbCategoriesRef.children.forEach { foreach ->
                        val categoryTitle = foreach.key.toString()
                        var categoryItem = foreach.value.toString()
                        if (categoryItem.endsWith(";"))
                            categoryItem = categoryItem.substring(0, categoryItem.length - 1)
                        val itemList: List<String> = categoryItem.split(";").map { its -> its.trim() }
                        val categoryItemList: MutableList<CategoryItem> = ArrayList()

                        itemList.forEach { value ->
                            val title = dbDBRef.child(value).key.toString()
                            val imageUrl = dbDBRef.child(value).child("poster_min").value.toString()
                            categoryItemList.add(CategoryItem(title, imageUrl))
                        }
                        allCategory.add(AllCategory(categoryTitle, categoryItemList))
                        setMainCategoryRecycler(allCategory)
                    }

                    finalCallback.onCallBack(previewTitle, previewUrl, userFavourite)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }

        })
    }

    private fun setMainCategoryRecycler (allCategory: List<AllCategory>) {

        mainCategoryRecycler = binding.categoryRV
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        mainCategoryRecycler!!.layoutManager = layoutManager
        mainRecyclerAdapter = context?.let { MainRecyclerAdapter(it, allCategory) }
        mainCategoryRecycler!!.adapter = mainRecyclerAdapter
    }
}