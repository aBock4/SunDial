package com.sundial.v1001.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sundial.v1001.dto.City

@Dao
interface ILocalCityDAO {

    @Query("SELECT * FROM cities")
    fun getAllCities() : LiveData<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cities: ArrayList<City>)

    @Delete
    fun delete(city : City)
}