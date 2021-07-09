package com.souvik.asteroidrader.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ApiResponse(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "absolute_magnitude") var absoluteMagnitude: String = "",
    @ColumnInfo(name = "estimated_diameter_max") var estimatedDiameterMax: String = "",
    @ColumnInfo(name = "is_potentially_hazardous_asteroid") var isPotentiallyHazard: Boolean = false,
    @ColumnInfo(name = "relative_velocity") var relativeVelocity: String = "",
    @ColumnInfo(name = "miss_distance") var missDistance: String = "",
    @ColumnInfo(name = "date") var date: String = ""
) : Serializable
