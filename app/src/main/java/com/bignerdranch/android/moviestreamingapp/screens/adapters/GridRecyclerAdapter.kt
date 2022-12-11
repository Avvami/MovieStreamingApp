package com.bignerdranch.android.moviestreamingapp.screens.adapters

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

class GridRecyclerAdapter (private val context : Context, private val item: List<CategoryItem>) :
    RecyclerView.Adapter<GridRecyclerAdapter.GridRecyclerViewHolder>() {

    private lateinit var clickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun onItemClickListener (listener: OnItemClickListener) {
        clickListener = listener
    }

    class GridRecyclerViewHolder (itemView : View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        init {
            imageView = itemView.findViewById(R.id.itemImage)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridRecyclerViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_item_grid, parent, false)
        return GridRecyclerViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: GridRecyclerViewHolder, position: Int) {
        Glide.with(context).load(item[position].imageUrl).apply(RequestOptions().dontTransform()).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return item.size
    }
}