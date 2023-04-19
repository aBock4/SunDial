package com.sundial.v1001.service

import com.sundial.v1001.CityRetrofitClientInstance
import com.sundial.v1001.dao.ICityDAO
import com.sundial.v1001.dto.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

interface ICityService{
    suspend fun fetchCities() : List<City>?
}

class CityService : ICityService {
    override suspend fun fetchCities(): List<City>? {
        return withContext(Dispatchers.IO) {
            val service = CityRetrofitClientInstance.retrofitInstance?.create(ICityDAO::class.java)
            val cities = async {service?.getAllCities()}
            val result = cities.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}