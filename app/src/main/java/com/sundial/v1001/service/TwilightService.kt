package com.sundial.v1001.service

import com.sundial.v1001.RetrofitClientInstance
import com.sundial.v1001.dao.ITwilightDAO
import com.sundial.v1001.dto.Twilight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

interface ITwilightService{
    suspend fun fetchTwilights() : List<Twilight>?
}
class TwilightService : ITwilightService{
    override suspend fun fetchTwilights() : List<Twilight>?{
        return withContext(Dispatchers.IO){
            val service = RetrofitClientInstance.retrofitInstance?.create(ITwilightDAO::class.java)
            val Twilight = async { service?.getAllTwilights() }
            var result = Twilight.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }

}