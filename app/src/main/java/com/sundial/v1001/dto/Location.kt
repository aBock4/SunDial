package com.sundial.v1001.dto

data class Location(
    var cityId: Int = 0,
    var locationId: String = "",
    var locationName: String = "",
    var longitude: String = "",
    var latitude: String = "",
    var sunrise: String = "",
    var sunset: String = ""
) {

    override fun toString(): String {
        return "$locationName $sunrise $sunset"
    }
}
