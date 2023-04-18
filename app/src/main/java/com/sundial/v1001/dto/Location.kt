package com.sundial.v1001.dto

data class Location(var locationId : String = "", var locationName : String = "", var longitude : String = "0.00", var latitude : String = "0.00", val sunrise : String = "", val sunset : String = "") {

        override fun toString(): String {
            return "$locationName $longitude $latitude $sunrise $sunset"
        }
}
