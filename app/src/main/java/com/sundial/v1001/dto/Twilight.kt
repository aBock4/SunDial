package com.sundial.v1001.dto

import java.util.Date
import com.google.gson.annotations.SerializedName

data class Twilight (@SerializedName("sunrise") var sunrise : Date, @SerializedName("sunset") var sunset : Date ){

}