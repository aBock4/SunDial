package com.sundial.v1001

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ApplicationViewModel(application : Application) : AndroidViewModel(application) {

    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData() = locationLiveData
    fun startLocationUpdates() {
        locationLiveData.startLocationUpdates()
    }

}