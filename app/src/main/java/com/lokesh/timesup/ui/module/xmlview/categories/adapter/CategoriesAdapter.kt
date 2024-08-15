package com.lokesh.timesup.ui.module.xmlview.categories.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lokesh.timesup.base.BaseRecyclerAdapter
import com.lokesh.timesup.commonlistener.ClickListener
import com.lokesh.timesup.databinding.LayoutCategoryRowBinding
import com.lokesh.timesup.util.onClick

@SuppressLint("all")
class CategoriesAdapter(private val context: Context, private val listener: ClickListener<String>) : BaseRecyclerAdapter<LayoutCategoryRowBinding>() {

    private val categories : ArrayList<String> = arrayListOf()

    override fun inflateLayout(parent: ViewGroup, layout: Int): LayoutCategoryRowBinding =
        LayoutCategoryRowBinding.inflate(LayoutInflater.from(context), parent, false)

    override fun onBindView(holder: MyViewHolder<LayoutCategoryRowBinding>, position: Int) {
        super.onBindView(holder, position)
        val category = categories[position]
        holder.binding.category.text = category
        holder.binding.categoryView onClick {
            listener.onClick(category)
        }
    }

    fun fillCategories(list: List<String>){
        categories.clear()
        categories.addAll(list)
        notifyDataSetChanged()
    }

    override fun itemCount(): Int = if (categories.isNotEmpty()) categories.size else 0
}
