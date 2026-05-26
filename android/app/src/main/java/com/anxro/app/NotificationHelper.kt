package com.anxro.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationHelper(
    private val context: Context
) {

    private val channelId = "anxro"

    init {

        val manager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        val channel =
            NotificationChannel(
                channelId,
                "Anxro Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )

        channel.description =
            "Anxro App Notifications"

        manager.createNotificationChannel(channel)
    }

    fun showNotification(
    title: String,
    text: String
) {

    val manager =
        context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

    val notification =
        NotificationCompat.Builder(
            context,
            channelId
        )
            .setSmallIcon(
                R.mipmap.ic_launcher
            )
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(
                NotificationCompat.PRIORITY_HIGH
            )
            .setAutoCancel(true)
            .build()

    manager.notify(
        System.currentTimeMillis().toInt(),
        notification
    )
}