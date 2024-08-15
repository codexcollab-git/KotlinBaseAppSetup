package com.lokesh.timesup.ui.module.xmlview.products.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.lokesh.timesup.base.BaseRecyclerAdapter
import com.lokesh.timesup.commonlistener.ClickListener
import com.lokesh.timesup.databinding.LayoutProductRowBinding
import com.lokesh.timesup.model.Product
import com.lokesh.timesup.util.onClick

@SuppressLint("all")
class ProductsAdapter(private val context: Context, private val listener: ClickListener<Product>) : BaseRecyclerAdapter<LayoutProductRowBinding>() {

    private val products : ArrayList<Product> = arrayListOf()

    override fun inflateLayout(parent: ViewGroup, layout: Int): LayoutProductRowBinding =
        LayoutProductRowBinding.inflate(LayoutInflater.from(context), parent, false)

    override fun onBindView(holder: MyViewHolder<LayoutProductRowBinding>, position: Int) {
        super.onBindView(holder, position)
        val product = products[position]

        Glide.with(context).load(product.thumbnail).into(holder.binding.image)
        holder.binding.heading.text = product.title
        holder.binding.subHeading.text = product.description
        holder.binding.clickView onClick {
            listener.onClick(product)
        }
    }

    fun fillProduct(list: List<Product>?){
        if (!list.isNullOrEmpty()){
            products.clear()
            products.addAll(list)
            notifyDataSetChanged()
        }
    }

    override fun itemCount(): Int = if (products.isNotEmpty()) products.size else 0
}
