package com.lokesh.appsetup.customdialog

import android.content.Context
import com.lokesh.appsetup.base.OptionModel
import com.lokesh.appsetup.commonlistener.ClickListener

class CustomDialogBuilder(private val context: Context) {
    var dialogInstance: CustomDialog
    private var cancelable = false
    private var showCloseButton = false
    private var heading: String? = null
    private var subHeading: String? = null
    private var confirmText: String? = null
    private var cancelText: String? = null
    private var confirmTextBg: Int? = null
    private var negativeTextBg: Int? = null
    private var positiveListener: ClickListener<CustomDialog>? = null
    private var negativeListener: ClickListener<CustomDialog>? = null
    private var list: ArrayList<OptionModel> = ArrayList()
    private var optionListener: ClickListener<OptionModel>? = null
    private var prevSelectedOption: OptionModel? = null

    init {
        dialogInstance = CustomDialog(context)
    }

    fun build(): CustomDialog {
        dialogInstance = CustomDialog(
            context,
            heading,
            subHeading,
            confirmText,
            cancelText,
            cancelable,
            confirmTextBg,
            negativeTextBg,
            showCloseButton,
            positiveListener,
            negativeListener
        )
        return dialogInstance
    }

    fun buildOptions(): CustomDialog {
        dialogInstance = CustomDialog(
            context,
            subHeading,
            confirmText,
            cancelable,
            list,
            confirmTextBg,
            showCloseButton,
            optionListener
        )
        return dialogInstance
    }

    fun buildSelectOptions(): CustomDialog {
        dialogInstance = CustomDialog(
            context,
            confirmText,
            cancelable,
            prevSelectedOption,
            list,
            confirmTextBg,
            showCloseButton,
            optionListener
        )
        return dialogInstance
    }

    fun buildLoader(): CustomDialog {
        dialogInstance = CustomDialog(context, cancelable)
        return dialogInstance
    }

    fun setHeadingText(headingText: String?): CustomDialogBuilder {
        this.heading = headingText
        return this
    }

    fun setConfirmTextBackground(drawableId: Int? = null): CustomDialogBuilder {
        this.confirmTextBg = drawableId
        return this
    }

    fun setNegativeTextBackground(drawableId: Int? = null): CustomDialogBuilder {
        this.negativeTextBg = drawableId
        return this
    }

    fun setOptionList(optionList: ArrayList<OptionModel>): CustomDialogBuilder {
        this.list = optionList
        return this
    }

    fun setSubHeadingText(subHeadingText: String?): CustomDialogBuilder {
        this.subHeading = subHeadingText
        return this
    }

    fun setCancelTextText(cancelText: String?): CustomDialogBuilder {
        this.cancelText = cancelText
        return this
    }

    fun setCancelable(cancelable: Boolean): CustomDialogBuilder {
        this.cancelable = cancelable
        return this
    }

    fun showCloseButton(showButton: Boolean): CustomDialogBuilder {
        this.showCloseButton = showButton
        return this
    }

    fun setConfirmText(confirmText: String?) {
        this.confirmText = confirmText
    }

    fun setOptionListener(
        listener: ClickListener<OptionModel>?
    ) {
        this.optionListener = listener
    }

    fun setPositiveButton(
        confirmText: String?,
        listener: ClickListener<CustomDialog>?
    ): CustomDialogBuilder {
        positiveListener = listener
        this.confirmText = confirmText
        return this
    }

    fun setNegativeButton(
        cancelText: String?,
        listener: ClickListener<CustomDialog>?
    ): CustomDialogBuilder {
        negativeListener = listener
        this.cancelText = cancelText
        return this
    }

    fun setOptionPositiveButton(
        selectedOption: OptionModel?,
        confirmText: String?,
        listener: ClickListener<OptionModel>?
    ): CustomDialogBuilder {
        this.prevSelectedOption = selectedOption
        this.optionListener = listener
        this.confirmText = confirmText
        return this
    }
}
