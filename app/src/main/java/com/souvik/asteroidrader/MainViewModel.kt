package com.souvik.asteroidrader

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.souvik.asteroidrader.model.ApiResponse
import com.souvik.asteroidrader.model.AppDatabase
import com.souvik.asteroidrader.network.ApiClient
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(val context: Application) : AndroidViewModel(context) {

    private val _url = MutableLiveData<String>()
    val imageUrl: LiveData<String>
        get() = _url

    private val _responseList = MutableLiveData<List<ApiResponse>>()
    val responseList: LiveData<List<ApiResponse>>
        get() = _responseList

    private val _recordCount = MutableLiveData<Int>()
    val recordCount: LiveData<Int>
        get() = _recordCount

    init {
        //getAsteroidData()
        loadDataFromDb()
        getImageOfDay()
    }

    fun loadDataFromDb() {
        val count = AppDatabase.getDatabase(context)?.getRecordCount() ?: 0
        if (count > 0) {
            _responseList.value = (AppDatabase.getDatabase(context)?.getAll())
        }
    }

    fun getImageOfDay() {
        val result = ApiClient.apiInterface?.getImageOfDay()
        result?.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body()
                        ?.has("media_type") == true && response.body()
                        ?.has("url") == true && response.body()?.get("media_type")?.asString.equals(
                        "image"
                    )
                ) {
                    _url.value = response.body()?.get("url")?.asString + "|${response.body()?.get("title")?.asString}"
                } else {
                    _url.value =
                        "https://apod.nasa.gov/apod/image/2001/STSCI-H-p2006a-h-1024x614.jpg" +"|Solar System"
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                _url.value = "|Image cannot be loaded"
            }

        })
    }

    /*fun getAsteroidData() {
        val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val result = ApiClient.apiInterface?.getAsteroidData(startDate = currentDate)
        result?.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    for (i in response.body()
                        ?.get("near_earth_objects")?.asJsonObject?.keySet()!!) {
                        for (j in response.body()
                            ?.get("near_earth_objects")?.asJsonObject!![i].asJsonArray) {
                            val data = ApiResponse()
                            data.apply {
                                id = j.asJsonObject["id"].asLong
                                name = j.asJsonObject["name"].asString
                                absoluteMagnitude =
                                    j.asJsonObject["absolute_magnitude_h"].asString + " au"
                                estimatedDiameterMax =
                                    j.asJsonObject["estimated_diameter"].asJsonObject["kilometers"].asJsonObject["estimated_diameter_max"].asString + " Km"
                                isPotentiallyHazard =
                                    j.asJsonObject["is_potentially_hazardous_asteroid"].asBoolean
                                relativeVelocity =
                                    j.asJsonObject["close_approach_data"].asJsonArray[0].asJsonObject["relative_velocity"].asJsonObject["kilometers_per_hour"].asString + " Km/hr"
                                missDistance =
                                    j.asJsonObject["close_approach_data"].asJsonArray[0].asJsonObject["miss_distance"].asJsonObject["astronomical"].asString + " au"
                                date = i
                            }
                            Log.d("Tag", "onResponse: data $data")
                            AppDatabase.getDatabase(context)?.insertAll(data)
                        }
                    }
                    Log.d(
                        "TAG",
                        "onResponse: ${(AppDatabase.getDatabase(context)?.getAll() ?: ArrayList())}"
                    )
                    _responseList.value = (AppDatabase.getDatabase(context)?.getAll())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.message}")
            }
        })
    }*/
}