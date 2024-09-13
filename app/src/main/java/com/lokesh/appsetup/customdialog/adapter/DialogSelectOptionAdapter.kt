package com.lokesh.appsetup.customdialog.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lokesh.appsetup.R
import com.lokesh.appsetup.base.OptionModel
import com.lokesh.appsetup.databinding.LayoutDialogOptionBinding

@SuppressLint("all")
class DialogSelectOptionAdapter(
    private val context: Context,
    private val optionList: ArrayList<OptionModel>
) :
    RecyclerView.Adapter<DialogSelectOptionAdapter.MyViewHolder>() {

    private var prevOption: OptionModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(LayoutDialogOptionBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val option = optionList[position]
        holder.binding.head.text = "${option.head}  ${if (option.sub_head != null && option.sub_head != "") option.sub_head else "" }"
        if (prevOption != null) {
            if (option.option_id == prevOption?.option_id) {
                holder.binding.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_radio_filled))
            } else {
                holder.binding.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_radio_empty))
            }
        }
        holder.binding.clickView.setOnClickListener {
            prevOption = option
            notifyDataSetChanged()
        }
    }

    fun setOption(option: OptionModel?) {
        if (option != null) {
            prevOption = option
            notifyDataSetChanged()
        }
    }

    fun getSelectedOption(): OptionModel? {
        return prevOption
    }

    override fun getItemCount(): Int = if (optionList.isNotEmpty()) optionList.size else 0

    class MyViewHolder(val binding: LayoutDialogOptionBinding) :
        RecyclerView.ViewHolder(binding.root)
}
