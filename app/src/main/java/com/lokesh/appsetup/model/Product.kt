package com.lokesh.appsetup.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    @SerializedName(value = "id")
    var id: Int? = null,
    @SerializedName(value = "title")
    var title: String? = null,
    @SerializedName(value = "description")
    var description: String? = null,
    @SerializedName(value = "price")
    var price: Float = 0.0f,
    @SerializedName(value = "discountPercentage")
    var discountPercentage: Float = 0.0f,
    @SerializedName(value = "rating")
    var rating: Float = 0.0f,
    @SerializedName(value = "stock")
    var stock: Float = 0.0f,
    @SerializedName(value = "brand")
    var brand: String? = null,
    @SerializedName(value = "category")
    var category: String? = null,
    @SerializedName(value = "thumbnail")
    var thumbnail: String? = null,
    @SerializedName(value = "images")
    var images: ArrayList<String>? = null
) : Parcelable