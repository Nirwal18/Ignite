package com.nirwal.ignite

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.nirwal.ignite.service.RandomWallpaperService

/**
 * Implementation of App Widget functionality.
 */
class RandomWallpaperWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val intentAction = Intent(context, RandomWallpaperService::class.java)
    val pendingIntent =
        PendingIntent.getService(
            context,
            /* requestCode = */ 0,
            Intent(intentAction),
            PendingIntent.FLAG_IMMUTABLE)


    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.random_wallpaper_widget)
    views.setOnClickPendingIntent(R.id.btn_change, pendingIntent )
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}