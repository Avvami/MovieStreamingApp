package com.bignerdranch.android.moviestreamingapp.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.model.Availability

class GridAvailableRecyclerAdapter (private val context : Context, private val item: List<Availability>) :
    RecyclerView.Adapter<GridAvailableRecyclerAdapter.GridAvailableRecyclerViewHolder>() {

    private lateinit var clickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun onItemClickListener (listener: OnItemClickListener) {
        clickListener = listener
    }

    class GridAvailableRecyclerViewHolder (itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        init {
            imageView = itemView.findViewById(R.id.iconImage)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridAvailableRecyclerViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_icon, parent, false)
        return GridAvailableRecyclerViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: GridAvailableRecyclerViewHolder, position: Int) {
        holder.imageView.setImageResource(item[position].icon)
    }

    override fun getItemCount(): Int {
        return item.size
    }
}