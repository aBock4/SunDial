package com.sundial.v1001.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sundial.v1001.dto.City

@Database(entities = arrayOf(City::class), version = 1)
abstract class CityDatabase : RoomDatabase() {
    abstract fun localCityDAO(): ILocalCityDAO
}