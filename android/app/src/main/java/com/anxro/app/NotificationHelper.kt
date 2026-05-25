package com.anxro.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class NotificationHelper(
    private val context: Context
) {

    fun showNotification(
        title: String,
        text: String
    ) {

        val manager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        val channel =
            NotificationChannel(
                "anxro",
                "Anxro",
                NotificationManager.IMPORTANCE_HIGH
            )

        manager.createNotificationChannel(channel)

        val notification =
            NotificationCompat.Builder(
                context,
                "anxro"
            )
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build()

        manager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}
