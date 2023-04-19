package com.sundial.v1001

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CityRetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://pkgstore.datahub.io/"

    val retrofitInstance : Retrofit?
        get() {
            // has this object been created yet?
            if (retrofit == null) {
                // create it
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}