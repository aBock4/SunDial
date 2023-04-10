package com.sundial.v1001.dto

class Location(var locationId : String = "", var locationName : String = "", val longitude : String, val latitude : String) {

        override fun toString(): String {
            return "$locationName $longitude $latitude"
        }
}
