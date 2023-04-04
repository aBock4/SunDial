package com.sundial.v1001.dto

import java.util.Date
import com.google.gson.annotations.SerializedName

/**
 * Data class for sunrise and sunset times
 *
 * @property sunrise Sunrise time
 * @property sunset Sunset time
 */
data class Twilight (@SerializedName("sunrise") var sunrise : Date, @SerializedName("sunset") var sunset : Date ){

}