package com.sundial.v1001.dto

data class Location(var locationId : String = "", var locationName : String = "", val longitude : String = "0.00", val latitude : String = "0.00") {

        override fun toString(): String {
            return "$locationName $longitude $latitude"
        }
}
