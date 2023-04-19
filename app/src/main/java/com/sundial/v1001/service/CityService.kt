package com.sundial.v1001.service

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import com.sundial.v1001.CityRetrofitClientInstance
import com.sundial.v1001.dao.ICityDAO
import com.sundial.v1001.dao.ILocalCityDAO
import com.sundial.v1001.dto.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import androidx.room.Room
import com.sundial.v1001.dao.CityDatabase

interface ICityService {
    suspend fun fetchCities() : List<City>?
    fun getLocalCityDAO(): ILocalCityDAO
}
class CityService(private val application: Application) : ICityService {

    private lateinit var db: CityDatabase

    override suspend fun fetchCities(): List<City>? {
        return withContext(Dispatchers.IO) {
            val service = CityRetrofitClientInstance.retrofitInstance?.create(ICityDAO::class.java)
            val cities = async {service?.getAllCities()}
            val result = cities.await()?.awaitResponse()?.body()
            updateLocalCities(result)
            return@withContext result
        }
    }

    private suspend fun updateLocalCities(cities : ArrayList<City>?) {
        try {
            cities?.let {
                val localCityDAO = getLocalCityDAO()
                localCityDAO.insertAll(cities)
            }
        } catch (e: Exception) {
            Log.e(TAG, "error saving cities ${e.message}")
    }
    }

    override fun getLocalCityDAO(): ILocalCityDAO {
        if (!this::db.isInitialized) {
            db = Room.databaseBuilder(application, CityDatabase::class.java, "mycities").build()
        }
        return db.localCityDAO()
    }
}