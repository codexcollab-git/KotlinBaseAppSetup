package com.lokesh.appsetup.customdialog.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import com.lokesh.appsetup.R
import com.lokesh.appsetup.base.OptionModel
import com.lokesh.appsetup.commonlistener.ClickConfirmListener
import com.lokesh.appsetup.commonlistener.ClickListener
import com.lokesh.appsetup.customdialog.CustomDialog
import com.lokesh.appsetup.customdialog.CustomDialogBuilder
import com.lokesh.appsetup.databinding.CustomDialogProgressLoaderBinding
import java.util.*

fun Context.showLoaderDialog(
    prevDialog: CustomDialogBuilder?
): CustomDialogBuilder {
    return if (prevDialog != null && prevDialog.dialogInstance.getLoaderDialog()?.isShowing == true) {
        prevDialog
    } else {
        val dialog = CustomDialogBuilder(this)
        dialog.setCancelable(false)
        dialog.buildLoader().showLoader()
        return dialog
    }
}

fun Context.showErrorDialog(
    errorMessage: String,
    positiveBtnText: String = getString(android.R.string.ok),
    prevDialog: CustomDialogBuilder?
): CustomDialogBuilder {
    return if (prevDialog != null && prevDialog.dialogInstance.getDialog().isShowing) {
        prevDialog
    } else {
        val dialog = CustomDialogBuilder(this)
        dialog.setSubHeadingText(errorMessage)
        dialog.setCancelable(true)
        dialog.setNegativeButton(positiveBtnText) { it.dismiss() }
        dialog.build().show()
        dialog
    }
}

fun Context.showErrorMessageWithFunctionality(
    successMsg: String,
    positiveBtnText: String = getString(R.string.dialog_cancel),
    prevDialog: CustomDialogBuilder?,
    listener: ClickListener<CustomDialog>
): CustomDialogBuilder {
    if (prevDialog != null && prevDialog.dialogInstance.getDialog().isShowing) {
        prevDialog.dialogInstance.getDialog().dismiss()
    }
    val dialog = CustomDialogBuilder(this)
    dialog.setHeadingText(null)
    dialog.setSubHeadingText(successMsg)
    dialog.setCancelable(false)
    dialog.setNegativeTextBackground(R.drawable.bg_dialog_ok_btn_red)
    dialog.setNegativeButton(positiveBtnText) {
        listener.onClick(it)
        it.dismiss()
    }
    dialog.build().show()
    return dialog
}

fun Context.showSuccessMessage(
    successMsg: String,
    positiveBtnText: String = getString(R.string.dialog_ok),
    prevDialog: CustomDialogBuilder?,
    listener: ClickListener<CustomDialog>
): CustomDialogBuilder {
    return if (prevDialog != null && prevDialog.dialogInstance.getDialog().isShowing) {
        prevDialog
    } else {
        val dialog = CustomDialogBuilder(this)
        dialog.setHeadingText(null)
        dialog.setSubHeadingText(successMsg)
        dialog.setCancelable(true)
        dialog.setNegativeTextBackground(R.drawable.bg_dialog_ok_btn)
        dialog.setNegativeButton(positiveBtnText) {
            listener.onClick(it)
            it.dismiss()
        }
        dialog.showCloseButton(true)
        dialog.build().show()
        dialog
    }
}

fun Context.showSelectOptionDialog(
    selectedOption: OptionModel? = null,
    list: ArrayList<OptionModel>,
    drawable: Int,
    positiveBtnText: String = getString(R.string.dialog_done),
    prevDialog: CustomDialogBuilder?,
    listener: ClickListener<OptionModel>
): CustomDialogBuilder {
    return if (prevDialog != null && prevDialog.dialogInstance.getDialog().isShowing) {
        prevDialog
    } else {
        val dialog = CustomDialogBuilder(this)
        dialog.setHeadingText(null)
        dialog.setSubHeadingText(null)
        dialog.setCancelable(true)
        dialog.setConfirmTextBackground(drawable)
        dialog.setOptionList(list)
        dialog.setOptionPositiveButton(selectedOption, positiveBtnText) {
            listener.onClick(it)
        }
        dialog.showCloseButton(true)
        dialog.buildSelectOptions().show()
        dialog
    }
}

fun Context.showSelectOptionDialogNonCancel(
    selectedOption: OptionModel? = null,
    list: ArrayList<OptionModel>,
    drawable: Int,
    positiveBtnText: String = getString(R.string.dialog_done),
    prevDialog: CustomDialogBuilder?,
    listener: ClickListener<OptionModel>
): CustomDialogBuilder {
    return if (prevDialog != null && prevDialog.dialogInstance.getDialog().isShowing) {
        prevDialog
    } else {
        val dialog = CustomDialogBuilder(this)
        dialog.setHeadingText(null)
        dialog.setSubHeadingText(null)
        dialog.setCancelable(false)
        dialog.setConfirmTextBackground(drawable)
        dialog.setOptionList(list)
        dialog.setOptionPositiveButton(selectedOption, positiveBtnText) {
            listener.onClick(it)
        }
        dialog.showCloseButton(true)
        dialog.buildSelectOptions().show()
        dialog
    }
}

fun Context.showOptionDialog(
    subHeading: String?,
    list: ArrayList<OptionModel>,
    drawable: Int,
    positiveBtnText: String = getString(android.R.string.ok),
    prevDialog: CustomDialogBuilder?,
    listener: ClickListener<OptionModel>
): CustomDialogBuilder {
    return if (prevDialog != null && prevDialog.dialogInstance.getDialog().isShowing) {
        prevDialog
    } else {
        val dialog = CustomDialogBuilder(this)
        dialog.setHeadingText(null)
        dialog.setSubHeadingText(subHeading)
        dialog.setCancelable(true)
        dialog.setConfirmTextBackground(drawable)
        dialog.setOptionList(list)
        dialog.setOptionListener(listener)
        dialog.setNegativeButton(positiveBtnText) { it.dismiss() }
        dialog.showCloseButton(true)
        dialog.buildOptions().show()
        dialog
    }
}

fun Context.showConfirmDialog(
    subHeading: String,
    drawable: Int,
    positiveBtnText: String = getString(R.string.dialog_ok),
    prevDialog: CustomDialogBuilder?,
    listener: ClickListener<CustomDialog>
): CustomDialogBuilder {
    if (prevDialog != null && prevDialog.dialogInstance.getDialog().isShowing) {
        prevDialog.dialogInstance.getDialog().dismiss()
    }
    val dialog = CustomDialogBuilder(this)
    dialog.setHeadingText(null)
    dialog.setSubHeadingText(subHeading)
    dialog.setCancelable(true)
    dialog.setConfirmTextBackground(drawable)
    dialog.setPositiveButton(positiveBtnText) { listener.onClick(it) }
    dialog.setNegativeButton(getString(android.R.string.cancel)) { it.dismiss() }
    dialog.build().show()
    return dialog
}

fun Context.showConfirmDialogNonCancelable(
    subHeading: String,
    drawable: Int,
    positiveBtnText: String = getString(R.string.dialog_ok),
    prevDialog: CustomDialogBuilder?,
    listener: ClickConfirmListener
): CustomDialogBuilder {
    if (prevDialog != null && prevDialog.dialogInstance.getDialog().isShowing) {
        prevDialog.dialogInstance.getDialog().dismiss()
    }
    val dialog = CustomDialogBuilder(this)
    dialog.setHeadingText(null)
    dialog.setSubHeadingText(subHeading)
    dialog.setCancelable(false)
    dialog.setConfirmTextBackground(drawable)
    dialog.setPositiveButton(positiveBtnText) {
        it.dismiss()
        listener.onSuccess()
    }
    dialog.setNegativeButton(getString(android.R.string.cancel)) {
        it.dismiss()
        listener.onCancel()
    }
    dialog.build().show()
    return dialog
}

fun Context.showPermissionRequired(
    permissionMsg: String,
    prevDialog: CustomDialogBuilder?
): CustomDialogBuilder {
    if (prevDialog != null && prevDialog.dialogInstance.getDialog().isShowing) {
        prevDialog.dialogInstance.getDialog()
            .cancel()
    }
    val dialog = CustomDialogBuilder(this)
    dialog.setHeadingText(getString(R.string.permission_required))
    dialog.setSubHeadingText(permissionMsg)
    dialog.setConfirmText(getString(R.string.go_to_settings))
    dialog.setNegativeButton(getString(android.R.string.cancel)) { it.dismiss() }
    dialog.setCancelable(false)
    dialog.build().show()
    return dialog
}

fun Context.showPermissionPermanentlyDenied(
    permissionMsg: String,
    prevDialog: CustomDialogBuilder?
): CustomDialogBuilder {
    if (prevDialog != null && prevDialog.dialogInstance.getDialog().isShowing) {
        prevDialog.dialogInstance.getDialog()
            .cancel()
    }
    val dialog = CustomDialogBuilder(this)
    dialog.setHeadingText(getString(R.string.permission_denied))
    dialog.setSubHeadingText(permissionMsg)
    dialog.setConfirmText(getString(R.string.dialog_go_to_setting))
    dialog.setNegativeButton(getString(android.R.string.cancel)) { it.dismiss() }
    dialog.setCancelable(false)
    dialog.build().show()
    return dialog
}

private var dialog: AppCompatDialog? = null
private lateinit var dialogBinding: CustomDialogProgressLoaderBinding

fun Context.showProgressLoader() {
    dialog = AppCompatDialog(this)
    dialogBinding = CustomDialogProgressLoaderBinding.inflate(LayoutInflater.from(this))
    dialog!!.setContentView(dialogBinding.root)
    dialog!!.setCancelable(false)
    if (dialog!!.window != null) dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    try {
        dialog!!.show()
    } catch (exp: WindowManager.BadTokenException) {
        Log.d("Logger", "showProgressLoader: -> $exp")
    }
}

fun Context.dismissProgressLoader() {
    if (dialog != null) dialog!!.dismiss()
}
