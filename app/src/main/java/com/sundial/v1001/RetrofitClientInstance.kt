package com.sundial.v1001

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object that creates a Retrofit instance for making API requests
 */
object RetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://api.sunrise-sunset.org/"

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