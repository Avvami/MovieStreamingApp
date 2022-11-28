package com.bignerdranch.android.moviestreamingapp.screens.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.moviestreamingapp.R
import com.bignerdranch.android.moviestreamingapp.model.AllCategory
import com.bignerdranch.android.moviestreamingapp.model.CategoryItem
import com.bignerdranch.android.moviestreamingapp.screens.fragments.DetailsFragment
import io.github.muddz.styleabletoast.StyleableToast

class MainRecyclerAdapter(private val context: Context, private val allCategory: List<AllCategory>) : RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var categoryTitle : TextView
        var categoryRecycler : RecyclerView

        init {
            categoryTitle = itemView.findViewById(R.id.categoryTV)
            categoryRecycler = itemView.findViewById(R.id.categoryItemRV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(context).inflate(R.layout.main_recycler_row_item, parent, false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.categoryTitle.text = allCategory[position].categoryTitle
        setCategoryItemRecycler(holder.categoryRecycler, allCategory[position].categoryItem)
    }

    override fun getItemCount(): Int {
        return allCategory.size
    }

    private fun setCategoryItemRecycler(recyclerView: RecyclerView, categoryItem: List<CategoryItem>) {

        val itemRecyclerAdapter = CategoryItemAdapter(context, categoryItem)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = itemRecyclerAdapter

        /*itemRecyclerAdapter.setOnItemClickListener(object : CategoryItemAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                *//*val intent = Intent(context, DetailsFragment::class.java)
                intent.putExtra("name", categoryItem[position].itemName)
                context.startActivity(intent)*//*
                StyleableToast.makeText(context, "You clicked on item named ${categoryItem[position].itemName}", Toast.LENGTH_SHORT, R.style.CustomToastStyle).show()
            }
        })*/
    }
}