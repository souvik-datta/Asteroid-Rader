package com.souvik.asteroidrader.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/planetary/apod")
    fun getImageOfDay(@Query("api_key") apiKey: String = "ujkfLl2jdL7zkQ78O1qHTIw1IssqnO0Amck3ft0g"): Call<JsonObject>

    @GET("/neo/rest/v1/feed")
    fun getAsteroidData(
        @Query("api_key") apiKey: String = "ujkfLl2jdL7zkQ78O1qHTIw1IssqnO0Amck3ft0g",
        @Query("start_date") startDate: String
    ): Call<JsonObject>
}