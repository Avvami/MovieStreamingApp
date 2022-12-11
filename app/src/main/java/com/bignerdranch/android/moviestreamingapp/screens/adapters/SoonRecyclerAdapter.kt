package com.bignerdranch.android.moviestreamingapp.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.model.SoonItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso

class SoonRecyclerAdapter (private val context : Context, private val item: List<SoonItem>) : RecyclerView.Adapter<SoonRecyclerAdapter.SoonRecyclerViewHolder>() {

    private lateinit var clickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun onItemClickListener (listener: OnItemClickListener) {
        clickListener = listener
    }

    class SoonRecyclerViewHolder (itemView : View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var logoView: ImageView
        var monthText: TextView
        var monthDateText: TextView
        var comingText: TextView
        var descriptionText: TextView
        private var trailerBtn: RelativeLayout

        init {
            imageView = itemView.findViewById(R.id.itemSoonImage)
            logoView = itemView.findViewById(R.id.itemSoonLogo)
            monthText = itemView.findViewById(R.id.monthTV)
            monthDateText = itemView.findViewById(R.id.monthNumTV)
            comingText = itemView.findViewById(R.id.comingTV)
            descriptionText = itemView.findViewById(R.id.soonDescription)
            trailerBtn = itemView.findViewById(R.id.trailerBtn)
            trailerBtn.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoonRecyclerViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.soon_item, parent, false)
        return SoonRecyclerViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: SoonRecyclerViewHolder, position: Int) {
        Glide.with(context).load(item[position].imageUrl).apply(RequestOptions().dontTransform()).into(holder.imageView)
        Glide.with(context).load(item[position].logoUrl).apply(RequestOptions().dontTransform()).into(holder.logoView)
        holder.monthText.text = item[position].month
        holder.monthDateText.text = item[position].monthDate
        holder.comingText.text = item[position].coming
        holder.descriptionText.text = item[position].description
    }

    override fun getItemCount(): Int {
        return item.size
    }
}