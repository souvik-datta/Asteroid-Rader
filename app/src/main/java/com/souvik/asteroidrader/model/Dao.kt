package com.souvik.asteroidrader.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg data: ApiResponse)

    @Query("Select * from ApiResponse order by date")
    fun getAll(): List<ApiResponse>

    @Query("Delete from ApiResponse")
    fun removeAll()

    @Query("Select count(1) from ApiResponse")
    fun getRecordCount() : Int
}