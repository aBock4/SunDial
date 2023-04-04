package com.sundial.v1001.service

import com.sundial.v1001.RetrofitClientInstance
import com.sundial.v1001.dao.ITwilightDAO
import com.sundial.v1001.dto.Twilight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

/**
 * An interface for getting Twilight data
 */
interface ITwilightService{
    suspend fun fetchTwilight() : List<Twilight>?
}

/**
 * Implementation of the ITwilightService interface which retrieves data using RetrofitClient
 */
class TwilightService : ITwilightService {
    override suspend fun fetchTwilight() : List<Twilight>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(ITwilightDAO::class.java)
            val data = async { service?.getData() }

            return@withContext data.await()?.awaitResponse()?.body()
        }
    }
}