package com.lokesh.appsetup.fcm.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.lokesh.appsetup.application.App
import com.lokesh.appsetup.fcm.model.DataObj
import com.lokesh.appsetup.fcm.model.FCMNotificationResponse
import com.lokesh.appsetup.fcm.model.NotificationObj
import com.lokesh.appsetup.model.Product
import org.json.JSONException

class FCMService : FirebaseMessagingService() {

    private var notificationUtil: NotificationUtils? = null

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCM NEW Token : ", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("FCM PAYLOAD", "" + remoteMessage.data)
        parseMessageJSON(remoteMessage)
    }

    private fun parseMessageJSON(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            try {
                val jsonObjStr = JsonParser.parseString(remoteMessage.data.toString()).asJsonObject
                val notification: FCMNotificationResponse = Gson().fromJson(
                    jsonObjStr,
                    FCMNotificationResponse::class.java
                )
                val notificationData = notification.dataObj
                if (notificationData != null) {
                    val dataObj = jsonObjStr?.get("data_obj")?.asJsonObject
                    val data = dataObj?.get("data")?.asJsonObject
                    val tweet: Product = Gson().fromJson(data, Product::class.java)
                    showNotificationMessage(notification.notificationObj!!, notification.dataObj!!, tweet)
                }
            } catch (e: Exception) {
                Log.e("FCM ERROR", "Exception: " + e.message)
            }
        }
    }

    private fun <T> showNotificationMessage(notification: NotificationObj, dataObj: DataObj, entityType: T) {
        try {
            notificationUtil = NotificationUtils(App.getAppContext())
            notificationUtil?.showNotificationMsg(notification, dataObj, entityType)
        } catch (e: JSONException) {
            Log.e("FCM ERROR", "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e("FCM ERROR", "Exception: " + e.message)
        }
    }
}
