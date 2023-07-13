package com.hpnightowl.clock.clockservice

import android.content.Intent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.util.Log
import com.hpnightowl.clock.ClockWidget

class ClockService : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //wake the device
        WakeService.acquire(context)

        //force widget update
        val widgetIntent = Intent(context, ClockWidget::class.java)
        widgetIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, ClockWidget::class.java))
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(widgetIntent)
        Log.d("WIDGET", "Widget set to update!")

        //go back to sleep
        WakeService.release()
    }
}