package com.sundial.v1001.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cities")
data class City(
    @SerializedName("country") var country: String = "",
    @SerializedName("geonameid") @PrimaryKey var id: Int = 0,
    @SerializedName("name") var cityName: String = ""
) {
    override fun toString(): String {
        return cityName
    }
}