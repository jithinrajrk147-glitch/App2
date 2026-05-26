package com.anxro.app

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.json.JSONObject
import java.net.URL

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {

        try {

            val jsonText =
                URL(
                    "https://github.com/jithinrajrk147-glitch/Sliser-/blob/main/notifications.json"
                ).readText()

            val json =
                JSONObject(jsonText)

            val newId =
                json.getInt("id")

            val title =
                json.getString("title")

            val message =
                json.getString("message")

            val page =
                json.getString("page")

            val prefs =
                applicationContext
                    .getSharedPreferences(
                        "anxro_notify",
                        Context.MODE_PRIVATE
                    )

            val oldId =
                prefs.getInt(
                    "last_id",
                    0
                )

            if (newId > oldId) {

                val intent =
                    Intent(
                        applicationContext,
                        MainActivity::class.java
                    )

                intent.putExtra(
                    "page",
                    page
                )

                NotificationHelper(
                    applicationContext
                ).showNotification(
                    title,
                    message
                )

                prefs.edit()
                    .putInt(
                        "last_id",
                        newId
                    )
                    .apply()
            }

            return Result.success()

        } catch (e: Exception) {

            e.printStackTrace()

            return Result.retry()
        }
    }
}