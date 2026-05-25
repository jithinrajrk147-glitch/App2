package com.anxro.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
                    android.R.drawable.ic_dialog_info
                )
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .build()

        manager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}
