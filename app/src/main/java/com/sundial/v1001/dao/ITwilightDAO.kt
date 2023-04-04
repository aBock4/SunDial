package com.sundial.v1001.dao

import com.sundial.v1001.dto.Twilight
import retrofit2.Call
import retrofit2.http.GET

/**
 * Data access object for retrieving twilight information
 */
interface ITwilightDAO {
    /**
     * Retrieves a list of twilight data
     *
     * @return A Call object that can be used to retrieve the data
     */
    @GET("/json")
    fun getData() : Call<ArrayList<Twilight>>
}