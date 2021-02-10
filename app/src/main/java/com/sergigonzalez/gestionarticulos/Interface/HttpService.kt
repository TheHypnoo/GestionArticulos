package com.sergigonzalez.gestionarticulos.Interface

import com.sergigonzalez.gestionarticulos.weather.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface HttpService {

    @GET("weather")
    fun getCity(
        @Query("q") city: String,
        @Query("appid") api: String,
        @Query("lang") lang: String
    ): Call<Weather>

    @GET
    fun getJSON(@Url url: String): Call<Weather>
}