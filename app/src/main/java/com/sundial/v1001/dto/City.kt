package com.sundial.v1001.dto

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName="plants")
data class City(@SerializedName("name") var cityName : String = "", @SerializedName("country") var country : String = "", @SerializedName("subcountry") var subCountry : String = "") {
    override fun toString(): String {
        return cityName
    }
}