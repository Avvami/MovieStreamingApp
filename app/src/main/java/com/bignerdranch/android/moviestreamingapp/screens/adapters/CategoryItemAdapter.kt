package com.bignerdranch.android.moviestreamingapp.screens.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bignerdranch.android.moviestreamingapp.screens.fragments.DetailsFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class CategoryItemAdapter(private val context: Context, private val categoryItem: List<CategoryItem>) : RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder>() {

    class CategoryItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        init {
            imageView = itemView.findViewById(R.id.itemImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
        return CategoryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        Glide.with(context).load(categoryItem[position].imageUrl).apply(RequestOptions().dontTransform()).into(holder.imageView)
        holder.imageView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("title", categoryItem[position].itemName)
            bundle.putString("preview_image", categoryItem[position].imageUrl)
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            val fragmentManager = (context as FragmentActivity).supportFragmentManager
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out).add(R.id.frameLayout, fragment).addToBackStack(null).commit()
        }
    }

    override fun getItemCount(): Int {
        return categoryItem.size
    }
}