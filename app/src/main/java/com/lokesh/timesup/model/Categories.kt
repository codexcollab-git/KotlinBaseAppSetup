package com.lokesh.timesup.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Categories(
    @SerializedName(value = "slug")
    var slug: String? = null,
    @SerializedName(value = "name")
    var name: String? = null,
    @SerializedName(value = "url")
    var url: String? = null,
) : Parcelable