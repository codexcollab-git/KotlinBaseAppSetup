package com.lokesh.appsetup.room.entity

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(primaryKeys = ["name"])
@Parcelize
data class CategoriesEntity(
    var slug: String = "",
    var name: String = ""
) : Parcelable