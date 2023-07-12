package com.hpnightowl.clock

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.RemoteViews

import org.json.JSONObject

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ClockWidget : AppWidgetProvider() {

    private val WEATHER_API_KEY = ""
    private val WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=London&appid=${WEATHER_API_KEY}"

    private val WEATHER_ICON_URL = "http://openweathermap.org/img/w/"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.clock_widget)

        // Get the current weather
        val weatherData = getWeatherData(context)

        // Set the weather in the widget
        if (weatherData != null) {
            val weather = weatherData.getJSONArray("weather").getJSONObject(0)
            val weatherIcon = weather.getString("icon")

            // Get the weather icon
            val iconUrl = WEATHER_ICON_URL + weatherIcon + ".png"
            val icon = getBitmapFromUrl(context, iconUrl)

            if (icon != null) {
                views.setImageViewBitmap(R.id.weather_icon, icon)
            }

            views.setTextViewText(R.id.weather, weather.getString("main"))
            views.setTextViewText(
                R.id.temperature,
                weatherData.getJSONObject("main").getString("temp") + "°C"
            )
        } else {
            views.setTextViewText(R.id.weather, "Not found")
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getWeatherData(context: Context): JSONObject? {
        val url = URL(WEATHER_API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.connect()

        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = reader.readText()
        reader.close()

        return JSONObject(response)
    }

    private fun getBitmapFromUrl(context: Context, url: String): Bitmap? {
        val imageUrl = URL(url)
        val inputStream = imageUrl.openStream()
        val drawable = BitmapDrawable(context.resources, inputStream)
        return drawable.bitmap
    }
}
