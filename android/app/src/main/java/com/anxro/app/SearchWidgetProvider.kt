package com.anxro.app

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class SearchWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        for (appWidgetId in appWidgetIds) {

            val views =
                RemoteViews(
                    context.packageName,
                    R.layout.widget_search
                )

            // OPEN browser.html

            val browserIntent =
                Intent(context, MainActivity::class.java)

            browserIntent.putExtra(
                "page",
                "browser.html"
            )

            val browserPending =
                PendingIntent.getActivity(
                    context,
                    1,
                    browserIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )

            views.setOnClickPendingIntent(
                R.id.open_browser,
                browserPending
            )

            views.setOnClickPendingIntent(
                R.id.open_search,
                browserPending
            )

            // OPEN earth.html

            val earthIntent =
                Intent(context, MainActivity::class.java)

            earthIntent.putExtra(
                "page",
                "earth.html"
            )

            val earthPending =
                PendingIntent.getActivity(
                    context,
                    2,
                    earthIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )

            views.setOnClickPendingIntent(
                R.id.open_earth,
                earthPending
            )

            appWidgetManager.updateAppWidget(
                appWidgetId,
                views
            )
        }
    }
}