package com.bignerdranch.android.moviestreamingapp.screens.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CategoryIconAdapter(private val context: Context, private val categoryIcon: List<CategoryItem>) : RecyclerView.Adapter<CategoryIconAdapter.CategoryIconViewHolder>() {

    private lateinit var dbRef: DatabaseReference

    class CategoryIconViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        init {
            imageView = itemView.findViewById(R.id.itemIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryIconViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_icon_item, parent, false)
        return CategoryIconViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryIconViewHolder, position: Int) {
        Glide.with(context).load(categoryIcon[position].imageUrl).apply(RequestOptions().dontTransform()).into(holder.imageView)
        dbRef = FirebaseDatabase.getInstance().reference
        holder.imageView.setOnClickListener {
            dbRef.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile_image").setValue(categoryIcon[position].imageUrl)
            (context as Activity).finish()
        }
    }

    override fun getItemCount(): Int {
        return categoryIcon.size
    }
}