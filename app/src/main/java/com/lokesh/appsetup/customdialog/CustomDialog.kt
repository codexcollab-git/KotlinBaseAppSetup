package com.lokesh.appsetup.customdialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.lokesh.appsetup.customdialog.adapter.DialogSelectOptionAdapter
import com.lokesh.appsetup.R
import com.lokesh.appsetup.base.OptionModel
import com.lokesh.appsetup.commonlistener.ClickListener
import com.lokesh.appsetup.customdialog.adapter.DialogOptionAdapter
import com.lokesh.appsetup.databinding.CustomDialogLayoutBinding
import com.lokesh.appsetup.databinding.CustomDialogProgressLoaderBinding
import com.lokesh.appsetup.util.goneView
import com.lokesh.appsetup.util.hideView
import com.lokesh.appsetup.util.onClick
import com.lokesh.appsetup.util.showView

class CustomDialog {
    private val context: Context
    private lateinit var loaderDialog: AppCompatDialog
    private lateinit var loaderDialogBinding: CustomDialogProgressLoaderBinding
    private lateinit var dialog: AppCompatDialog
    private lateinit var dialogBinding: CustomDialogLayoutBinding
    private var positiveListener: ClickListener<CustomDialog>? = null
    private var negativeListener: ClickListener<CustomDialog>? = null
    private var list: ArrayList<OptionModel> = ArrayList()
    private var optionListener: ClickListener<OptionModel>? = null

    constructor(context: Context) {
        this.context = context
        dialog = AppCompatDialog(context, R.style.CustomDialog)
        dialogBinding = CustomDialogLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)
        closeDialog()
        if (dialog.window != null) dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    constructor(
        context: Context,
        heading: String?,
        subHeading: String?,
        confirmTxt: String?,
        cancelTxt: String?,
        cancelable: Boolean,
        confirmTextBg: Int?,
        negativeTextBg: Int?,
        showCloseButton: Boolean = false,
        positiveListener: ClickListener<CustomDialog>?,
        negativeListener: ClickListener<CustomDialog>?
    ) {
        this.context = context
        dialog = AppCompatDialog(context, R.style.CustomDialog)
        dialogBinding = CustomDialogLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        if (dialog.window != null) dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(cancelable)
        dialogBinding.cancel.hideView()
        if (heading == null || heading.isEmpty()) {
            dialogBinding.heading.goneView()
        } else {
            setHeading(
                heading
            )
        }
        if (subHeading == null || subHeading.isEmpty()) {
            dialogBinding.subHeading.goneView()
        } else {
            setSubHeading(
                subHeading
            )
        }
        if (confirmTxt == null || confirmTxt.isEmpty()) {
            dialogBinding.ok.goneView()
        } else {
            setPositiveLabel(
                confirmTxt
            )
        }
        if (cancelTxt == null || cancelTxt.isEmpty()) {
            dialogBinding.cancel.goneView()
        } else {
            setNegativeLabel(
                cancelTxt
            )
        }
        if (showCloseButton) {
            dialogBinding.close.showView()
            dialogBinding.spaceView.goneView()
        } else {
            dialogBinding.close.goneView()
            dialogBinding.spaceView.showView()
        }
        if (confirmTextBg != null) dialogBinding.ok.background = ContextCompat.getDrawable(context, confirmTextBg)
        if (negativeTextBg != null) dialogBinding.cancel.background = ContextCompat.getDrawable(context, negativeTextBg)
        if (positiveListener != null) this.positiveListener = positiveListener
        if (negativeListener != null) this.negativeListener = negativeListener
        closeDialog()
        initConfig()
    }

    constructor(
        context: Context,
        subHeading: String?,
        confirmTxt: String?,
        cancelable: Boolean,
        optionList: ArrayList<OptionModel>,
        confirmTextBg: Int?,
        showCloseButton: Boolean = false,
        listener: ClickListener<OptionModel>?
    ) {
        this.context = context
        dialog = AppCompatDialog(context, R.style.CustomDialog)
        dialogBinding = CustomDialogLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        if (dialog.window != null) dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(cancelable)
        dialogBinding.ok.goneView()
        dialogBinding.heading.goneView()
        if (subHeading == null || subHeading.isEmpty()) {
            dialogBinding.subHeading.goneView()
        } else {
            setSubHeading(
                subHeading
            )
        }
        if (confirmTxt == null || confirmTxt.isEmpty()) {
            dialogBinding.ok.goneView()
        } else {
            setPositiveLabel(
                confirmTxt
            )
        }
        if (showCloseButton) {
            dialogBinding.close.showView()
            dialogBinding.spaceView.goneView()
        } else {
            dialogBinding.close.goneView()
            dialogBinding.spaceView.showView()
        }
        if (optionList.isNotEmpty()) {
            dialogBinding.optionRecycler.showView()
            list.clear()
            list.addAll(optionList)
        }
        if (confirmTextBg != null) {
            dialogBinding.cancel.background =
                ContextCompat.getDrawable(context, confirmTextBg)
        }
        if (listener != null) this.optionListener = listener
        initRecyclerConfig(context)
        closeDialog()
        initConfig()
    }

    constructor(
        context: Context,
        confirmTxt: String?,
        cancelable: Boolean,
        prevSelectedOption: OptionModel?,
        optionList: ArrayList<OptionModel>,
        confirmTextBg: Int?,
        showCloseButton: Boolean = false,
        listener: ClickListener<OptionModel>?
    ) {
        this.context = context
        dialog = AppCompatDialog(context, R.style.CustomDialog)
        dialogBinding = CustomDialogLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        if (dialog.window != null) dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(cancelable)
        dialogBinding.headView.goneView()
        dialogBinding.ok.goneView()
        if (confirmTxt == null || confirmTxt.isEmpty()) {
            dialogBinding.cancel.goneView()
        } else {
            setNegativeLabel(
                confirmTxt
            )
        }
        if (showCloseButton) {
            dialogBinding.close.showView()
            dialogBinding.spaceView.goneView()
        } else {
            dialogBinding.close.goneView()
            dialogBinding.spaceView.showView()
        }
        if (optionList.isNotEmpty()) {
            dialogBinding.optionRecycler.showView()
            list.clear()
            list.addAll(optionList)
        }
        if (confirmTextBg != null) dialogBinding.cancel.background = ContextCompat.getDrawable(context, confirmTextBg)
        if (listener != null) this.optionListener = listener
        initRecyclerConfig(context, prevSelectedOption)
        closeDialog()
        // initConfig()
    }

    constructor(context: Context, cancelable: Boolean) {
        this.context = context
        loaderDialog = AppCompatDialog(context)
        loaderDialogBinding = CustomDialogProgressLoaderBinding.inflate(LayoutInflater.from(context))
        loaderDialog.setContentView(loaderDialogBinding.root)
        loaderDialog.setCancelable(cancelable)
        if (loaderDialog.window != null) {
            loaderDialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                    Color.TRANSPARENT
                )
            )
        }
        closeDialog()
    }

    private fun closeDialog() {
        if (this::dialogBinding.isInitialized) {
            dialogBinding.close onClick { if (this::dialog.isInitialized) dialog.dismiss() }
        }
    }

    fun getLoaderDialog(): AppCompatDialog? {
        return if (this::loaderDialog.isInitialized) { loaderDialog } else null
    }

    fun showLoader() {
        if (!(context as Activity).isFinishing) loaderDialog.show()
    }

    fun dismissLoader() {
        if (this::loaderDialog.isInitialized) {
            loaderDialog.dismiss()
        }
    }

    fun getDialog(): AppCompatDialog {
        return dialog
    }

    fun show() {
        if (!(context as Activity).isFinishing) dialog.show()
    }

    fun dismiss() {
        if (!(context as Activity).isFinishing && !context.isDestroyed) dialog.dismiss()
    }

    fun setHeading(title: String?) {
        dialogBinding.heading.text = title
    }

    fun setSubHeading(subTitle: String?) {
        dialogBinding.subHeading.text = subTitle
    }

    private fun setPositiveLabel(positive: String) {
        dialogBinding.ok.showView()
        dialogBinding.ok.text = positive
    }

    private fun setNegativeLabel(negative: String) {
        dialogBinding.cancel.showView()
        dialogBinding.cancel.text = negative
    }

    private fun initRecyclerConfig(context: Context) {
        val optionAdapter = DialogOptionAdapter(context, list) {
            if (optionListener != null) optionListener?.onClick(it)
            dialog.dismiss()
        }
        dialogBinding.optionRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = optionAdapter
        }
    }

    private fun initRecyclerConfig(context: Context, prevOption: OptionModel?) {
        val optionAdapter = DialogSelectOptionAdapter(context, list)
        dialogBinding.optionRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = optionAdapter
        }
        optionAdapter.setOption(prevOption)
        dialogBinding.cancel.setOnClickListener {
            if (optionAdapter.getSelectedOption() != null && optionListener != null) {
                optionListener?.onClick(optionAdapter.getSelectedOption()!!)
                dialog.dismiss()
            }
        }
    }

    private fun initConfig() {
        // dialogBinding.cancel.setOnClickListener { dismiss() }
        dialogBinding.ok.setOnClickListener {
            if (positiveListener != null) positiveListener!!.onClick(this@CustomDialog)
        }
        dialogBinding.cancel.setOnClickListener {
            if (negativeListener != null) negativeListener!!.onClick(this@CustomDialog) else dismiss()
        }
    }

    fun setOnConfirmClickListener(listener: ClickListener<CustomDialog>?) {
        positiveListener = listener
    }

    fun setOnCancelListener(listener: ClickListener<CustomDialog>?) {
        negativeListener = listener
    }
}