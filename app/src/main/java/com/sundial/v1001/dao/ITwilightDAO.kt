package com.sundial.v1001.dao

import com.sundial.v1001.dto.Twilight
import retrofit2.Call
import retrofit2.http.GET

interface ITwilightDAO {

    @GET("/json")
    internal fun getData() : Call<ArrayList<Twilight>>
}