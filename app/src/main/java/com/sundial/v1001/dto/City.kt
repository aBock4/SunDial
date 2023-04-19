package com.sundial.v1001.dto

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName="plants")
data class City(@SerializedName("country") var country : String = "", @SerializedName("geonameid") var cityId : Int = 0 ,@SerializedName("name") var cityName : String = "",@SerializedName("subcountry") var subCountry : String = "") {
    override fun toString(): String {
        return cityName
    }
}