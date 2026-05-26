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

        manager.createNotificationChannel(
            channel
        )
    }

    fun showNotification(
        title: String,
        text: String
    ) {

        val intent =
            Intent(
                context,
                MainActivity::class.java
            )

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
            Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                PendingIntent.FLAG_IMMUTABLE
            )

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
                .setContentIntent(
                    pendingIntent
                )
                .setAutoCancel(true)
                .build()

        manager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}