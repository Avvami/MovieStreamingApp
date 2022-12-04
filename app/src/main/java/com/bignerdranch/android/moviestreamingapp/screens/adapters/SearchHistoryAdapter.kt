package com.bignerdranch.android.moviestreamingapp.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SearchHistoryAdapter (private val context : Context, private val item: MutableList<CategoryItem>) : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryRecyclerViewHolder>() {

    private lateinit var clickListener: OnItemClickListener
    private lateinit var longClickListener: OnItemLongListener

    interface OnItemLongListener {
        fun onItemLongClick(position: Int)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun onItemClickListener (listener: OnItemClickListener) {
        clickListener = listener
    }

    fun onItemLongClickListener (longListener: OnItemLongListener) {
        longClickListener = longListener
    }

    class SearchHistoryRecyclerViewHolder (itemView: View, listener: OnItemClickListener, longListener: OnItemLongListener) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var titleText: TextView

        init {
            imageView = itemView.findViewById(R.id.previewImage)
            titleText = itemView.findViewById(R.id.titleTV)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            itemView.setOnLongClickListener {
                longListener.onItemLongClick(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryRecyclerViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false)
        return SearchHistoryRecyclerViewHolder(itemView, clickListener, longClickListener)
    }

    override fun onBindViewHolder(holder: SearchHistoryRecyclerViewHolder, position: Int) {
        Glide.with(context).load(item[position].imageUrl).apply(RequestOptions().dontTransform()).into(holder.imageView)
        holder.titleText.text = item[position].itemName
    }

    override fun getItemCount(): Int {
        return item.size
    }
}