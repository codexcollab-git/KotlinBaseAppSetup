package com.lokesh.appsetup.room.entity

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(primaryKeys = ["id", "category"])
@Parcelize
data class ProductEntity(
    var id: Int = 0,
    var title: String? = null,
    var description: String? = null,
    var price: Float = 0.0f,
    var discountPercentage: Float = 0.0f,
    var rating: Float = 0.0f,
    var stock: Float = 0.0f,
    var brand: String? = null,
    var category: String = "",
    var thumbnail: String? = null,
    var images: String? = null
) : Parcelable