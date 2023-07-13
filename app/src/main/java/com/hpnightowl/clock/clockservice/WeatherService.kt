package com.hpnightowl.clock.clockservice

import android.annotation.SuppressLint
import com.hpnightowl.clock.model.data
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

class WeatherService {


    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(WeatherInterface::class.java)




    companion object {
        const val LOCATION_PREFERENCE = "location_pref"
        const val LOCATION_PREFERENCE_LATITUDE = "location_pref_lat"
        const val LOCATION_PREFERENCE_LONGITUDE = "location_pref_long"
    }

}

interface WeatherInterface {
    @GET("weather?&units=metric")
    fun getData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String
    ): Call<data>
}

@SuppressLint("DefaultLocale")
fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { s ->
        s.lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }