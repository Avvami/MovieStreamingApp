package com.bignerdranch.android.moviestreamingapp.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.model.AllCategory
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem

class MainRecyclerIconAdapter(private val context: Context, private val allCategory: List<AllCategory>) : RecyclerView.Adapter<MainRecyclerIconAdapter.MainIconViewHolder>() {

    class MainIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var categoryTitle : TextView
        var categoryRecycler : RecyclerView

        init {
            categoryTitle = itemView.findViewById(R.id.categoryTV)
            categoryRecycler = itemView.findViewById(R.id.categoryItemRV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainIconViewHolder {
        return MainIconViewHolder(LayoutInflater.from(context).inflate(R.layout.main_recycler_row_item, parent, false))
    }

    override fun onBindViewHolder(holder: MainIconViewHolder, position: Int) {
        holder.categoryTitle.text = allCategory[position].categoryTitle
        setCategoryIconRecycler(holder.categoryRecycler, allCategory[position].categoryItem)
    }

    override fun getItemCount(): Int {
        return allCategory.size
    }

    private fun setCategoryIconRecycler(recyclerView: RecyclerView, categoryIcon: List<CategoryItem>) {
        val iconRecyclerAdapter = CategoryIconAdapter(context, categoryIcon)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = iconRecyclerAdapter
    }
}