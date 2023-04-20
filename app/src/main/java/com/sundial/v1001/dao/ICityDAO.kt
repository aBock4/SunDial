package com.sundial.v1001.dao

import com.sundial.v1001.dto.City
import retrofit2.Call
import retrofit2.http.GET

interface ICityDAO {

    @GET("/core/world-cities/world-cities_json/data/5b3dd46ad10990bca47b04b4739a02ba/world-cities_json.json")
    fun getAllCities(): Call<ArrayList<City>>
}