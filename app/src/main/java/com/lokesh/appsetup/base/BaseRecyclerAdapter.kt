package com.lokesh.appsetup.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

@SuppressLint("all")
abstract class BaseRecyclerAdapter<VB : ViewBinding> : RecyclerView.Adapter<BaseRecyclerAdapter.MyViewHolder<VB>>() {

    private lateinit var binding: VB

    abstract fun inflateLayout(parent: ViewGroup, layout: Int): VB

    override fun onCreateViewHolder(parent: ViewGroup, layout: Int): MyViewHolder<VB> {
        binding = inflateLayout(parent, layout)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder<VB>, position: Int) {
        onBindView(holder, position)
    }

    open fun onBindView(holder: MyViewHolder<VB>, position: Int) {
    }

    override fun getItemCount(): Int = itemCount()

    open fun itemCount(): Int = 0

    class MyViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
