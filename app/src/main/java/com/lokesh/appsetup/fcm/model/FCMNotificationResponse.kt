package com.lokesh.appsetup.fcm.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FCMNotificationResponse(
    @field:SerializedName("notification_obj")
    var notificationObj: NotificationObj? = null,
    @field:SerializedName("data_obj")
    var dataObj: DataObj? = null
) : Parcelable

@Parcelize
data class NotificationObj(
    @field:SerializedName("badge")
    var badge: Int? = null,
    @field:SerializedName("title")
    var title: String? = null,
    @field:SerializedName("sound")
    var sound: String? = null,
    @field:SerializedName("body")
    var body: String? = null
) : Parcelable

@Parcelize
data class DataObj(
    @field:SerializedName("some_data")
    var someData: String? = null,
    @field:SerializedName("user_id")
    var userId: Int? = null
) : Parcelable
