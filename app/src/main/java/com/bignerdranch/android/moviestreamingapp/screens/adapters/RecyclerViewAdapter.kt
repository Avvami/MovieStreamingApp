package com.bignerdranch.android.moviestreamingapp.screens.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.data.Resource
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(private val resourceList : ArrayList<Resource>) : RecyclerView.Adapter<RecyclerViewAdapter.RVViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.resource_item, parent, false)

        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {

        val currentItem = resourceList[position]

        Picasso.get().load(currentItem.poster).into(holder.resourcePoster)
        holder.resourceName.text = currentItem.name
        holder.resourceGenre.text = currentItem.genre
        holder.resourceYear.text = currentItem.year
    }

    override fun getItemCount(): Int {

        return resourceList.size
    }

    class RVViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val resourcePoster : ImageView = itemView.findViewById(R.id.resource_image)
        val resourceName : TextView = itemView.findViewById(R.id.resource_name)
        val resourceGenre : TextView = itemView.findViewById(R.id.resource_genre)
        val resourceYear : TextView = itemView.findViewById(R.id.resource_year)
    }
}