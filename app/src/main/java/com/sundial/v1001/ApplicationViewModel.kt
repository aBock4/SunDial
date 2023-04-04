package com.sundial.v1001

import android.app.Application
import androidx.lifecycle.AndroidViewModel

/**
 * ViewModel for the app
 *
 * @param application The application object
 */
class ApplicationViewModel(application : Application) : AndroidViewModel(application) {

    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData() = locationLiveData
    fun startLocationUpdates() {
        locationLiveData.startLocationUpdates()
    }

}