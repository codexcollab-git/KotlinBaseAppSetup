package com.lokesh.appsetup.ui.module.xmlview.categories.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lokesh.appsetup.base.BaseRecyclerAdapter
import com.lokesh.appsetup.commonlistener.ClickListener
import com.lokesh.appsetup.databinding.LayoutCategoryRowBinding
import com.lokesh.appsetup.model.Categories
import com.lokesh.appsetup.util.onClick

@SuppressLint("all")
class CategoriesAdapter(private val context: Context, private val listener: ClickListener<Categories>) : BaseRecyclerAdapter<LayoutCategoryRowBinding>() {

    private val categories: ArrayList<Categories> = arrayListOf()

    override fun inflateLayout(parent: ViewGroup, layout: Int): LayoutCategoryRowBinding =
        LayoutCategoryRowBinding.inflate(LayoutInflater.from(context), parent, false)

    override fun onBindView(holder: MyViewHolder<LayoutCategoryRowBinding>, position: Int) {
        super.onBindView(holder, position)
        val category = categories[position]
        holder.binding.category = category
        holder.binding.categoryView onClick {
            listener.onClick(category)
        }
    }

    fun fillCategories(list: List<Categories>) {
        categories.clear()
        categories.addAll(list)
        notifyDataSetChanged()
    }

    override fun itemCount(): Int = if (categories.isNotEmpty()) categories.size else 0
}
