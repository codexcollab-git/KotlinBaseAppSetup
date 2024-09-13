package com.lokesh.appsetup.ui.module.xmlview.products.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.lokesh.appsetup.model.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetProductsResponse(
    @field:SerializedName("status")
    var status: Boolean = false,
    @field:SerializedName("message")
    var message: String? = null,
    @field:SerializedName("products")
    var products: List<Product> = arrayListOf()
) : Parcelable