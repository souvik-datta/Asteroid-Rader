package com.souvik.asteroidrader.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.JsonObject
import com.souvik.asteroidrader.model.ApiResponse
import com.souvik.asteroidrader.model.AppDatabase
import com.souvik.asteroidrader.network.ApiClient
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DownloadDataWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return getAsteroidData()
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
                           AppDatabase.getDatabase(applicationContext)?.insertAll(data)
                       }
                   }
               }
           }

           override fun onFailure(call: Call<JsonObject>, t: Throwable) {
               Log.d("TAG", "onFailure: ${t.message}")
           }
       })
   }*/

    fun getAsteroidData(): Result {
        return with(Dispatchers.IO) {
            val currentDate: String =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val response = ApiClient.apiInterface?.getAsteroidData(startDate = currentDate)?.execute()
            if (response?.isSuccessful == true) {
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
                        AppDatabase.getDatabase(applicationContext)?.insertAll(data)
                    }
                }
                Result.success()
            } else {
                Result.failure()
            }
        }
    }
}