package com.lokesh.timesup.fcm.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.lokesh.timesup.R
import com.lokesh.timesup.application.App
import com.lokesh.timesup.fcm.model.DataObj
import com.lokesh.timesup.fcm.model.NotificationObj
import com.lokesh.timesup.model.Product
import com.lokesh.timesup.ui.module.compose.MainActivity
import com.lokesh.timesup.util.BUNDLE_DATA
import com.lokesh.timesup.util.IS_COMING_FROM
import com.lokesh.timesup.util.NOTIFICATIONS
import com.lokesh.timesup.util.TWEET_CATEGORY
import com.lokesh.timesup.util.getUniqueNo

class NotificationUtils(private val context: Context) {

    private val channelId = context.getString(R.string.default_notification_channel_id)
    private val channelName = context.getString(R.string.default_notification_channel_name)
    private var uniqueCode = 0

    fun <T> showNotificationMsg(notification: NotificationObj, dataObj: DataObj, entityType: T) {
        uniqueCode = getUniqueNo()
        val bundle = Bundle()
        when (entityType) {
            is Product -> {
                val tweet: Product = entityType
                bundle.putString(TWEET_CATEGORY, tweet.category)
            }
        }
        val resultIntent = Intent(App.getAppContext(), MainActivity::class.java)
        bundle.putString(IS_COMING_FROM, NOTIFICATIONS)
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        resultIntent.putExtra(BUNDLE_DATA, bundle)
        showNotification(resultIntent, notification)
    }

    private fun showNotification(resultIntent: Intent, notification: NotificationObj) {
        val resultPendingIntent = TaskStackBuilder.create(context)
            .addNextIntent(resultIntent)
            .getPendingIntent(
                uniqueCode,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        val mBuilder = NotificationCompat.Builder(context, channelId)
        if (resultPendingIntent != null) {
            showSmallNotification(mBuilder, notification, resultPendingIntent)
        }
    }

    private fun showSmallNotification(
        mBuilder: NotificationCompat.Builder,
        notifiObj: NotificationObj,
        pendingIntent: PendingIntent,
    ) {

        /** -------  Remove this Code when notification click functionality gets implemented
         val resultIntent = Intent(App.getAppContext(), SplashActivity::class.java)
         val pendingIntent = PendingIntent.getActivity(context, uniqueCode, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
         * ------- */

        val icon = R.mipmap.ic_launcher
        val notification: Notification = mBuilder.setSmallIcon(icon).setTicker(notifiObj.title)
            .setAutoCancel(true)
            .setContentTitle(notifiObj.title)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, icon))
            .setContentText(notifiObj.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent/*if (!IS_APP_ACTIVE) pendingIntent else null*/)
            .build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(uniqueCode, notification)
    }
}
