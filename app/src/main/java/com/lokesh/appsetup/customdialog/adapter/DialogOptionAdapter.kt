package com.lokesh.appsetup.customdialog.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lokesh.appsetup.R
import com.lokesh.appsetup.base.OptionModel
import com.lokesh.appsetup.commonlistener.ClickListener
import com.lokesh.appsetup.databinding.LayoutDialogOptionBinding

@SuppressLint("all")
class DialogOptionAdapter(
    private val context: Context,
    private val optionList: ArrayList<OptionModel>,
    private val listener: ClickListener<OptionModel>
) :
    RecyclerView.Adapter<DialogOptionAdapter.MyViewHolder>() {

    private var selectPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(LayoutDialogOptionBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val option = optionList[position]
        holder.binding.head.text = option.head
        holder.binding.clickView.setOnClickListener {
            selectPos = position
            listener.onClick(option)
            notifyDataSetChanged()
        }
        if (selectPos == position) {
            holder.binding.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_radio_filled))
        } else {
            holder.binding.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_radio_empty))
        }
    }

    override fun getItemCount(): Int = if (optionList.isNotEmpty()) optionList.size else 0

    class MyViewHolder(val binding: LayoutDialogOptionBinding) :
        RecyclerView.ViewHolder(binding.root)
}
