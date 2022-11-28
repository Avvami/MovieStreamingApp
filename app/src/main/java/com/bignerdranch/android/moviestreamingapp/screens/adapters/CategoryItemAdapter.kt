package com.bignerdranch.android.moviestreamingapp.screens.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bignerdranch.android.moviestreamingapp.screens.activities.MainActivity
import com.bignerdranch.android.moviestreamingapp.screens.fragments.DetailsFragment
import com.bignerdranch.android.moviestreamingapp.screens.fragments.HomeFragment
import com.bumptech.glide.Glide
import io.github.muddz.styleabletoast.StyleableToast

class CategoryItemAdapter(private val context: Context, private val categoryItem: List<CategoryItem>) : RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder>() {

    /*private lateinit var clickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }*/

    class CategoryItemViewHolder(itemView : View/*, listener: OnItemClickListener*/) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        init {
            imageView = itemView.findViewById(R.id.itemImage)

            /*itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
        return CategoryItemViewHolder(itemView/*, clickListener*/)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        Glide.with(context).load(categoryItem[position].imageUrl).into(holder.imageView)
        holder.imageView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("title", categoryItem[position].itemName)
            bundle.putString("preview_image", categoryItem[position].imageUrl)
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            val fragmentManager = (context as FragmentActivity).supportFragmentManager
            fragmentManager.beginTransaction().add(R.id.frameLayout, fragment).addToBackStack(null).commit()
            //StyleableToast.makeText(context, "You clicked on item named ${activity.supportFragmentManager.findFragmentById(R.id.frameLayout)}", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show()
        }
    }

    override fun getItemCount(): Int {
        return categoryItem.size
    }
}