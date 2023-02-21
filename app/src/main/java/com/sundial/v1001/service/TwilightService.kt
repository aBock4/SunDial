package com.sundial.v1001.service

import com.sundial.v1001.RetrofitClientInstance
import com.sundial.v1001.dao.ITwilightDAO
import com.sundial.v1001.dto.Twilight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

interface ITwighlightService{
    suspend fun fetchData() : List<Twilight>?
}
class TwilightService : ITwighlightService{
    override suspend fun fetchData() : List<Twilight>?{
        return withContext(Dispatchers.IO){
            val service = RetrofitClientInstance.retrofitInstance?.create(ITwilightDAO::class.java)
            val data = async {service?.getData()}
            var result = data.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }

}