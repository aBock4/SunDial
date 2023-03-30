package com.sundial.v1001

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.sundial.v1001.dto.LocationDetails
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
//Change the variable context can be private.
class LocationLiveData(private var context: Context) : LiveData<LocationDetails>() {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun onActive() {
        super.onActive()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            location -> location.also {
                setLocationData(it)
            }
        }
        startLocationUpdates()
    }

    internal fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
    //Change: the location name is shadowed, as its states in the let lambda, you can replace the
    //name in the lambda with simply the value.
    private fun setLocationData(location: Location?) {
        location?.let {
        value = LocationDetails(location.longitude.toString(), location.latitude.toString())
        }
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    //Change: Elvis Operator always returns the left operand so its useless, and the expression is
    // Never used.
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }
    //Change: ONE_MINUTE is a constant so marking it as constant is good, its also not used
    //in another area of the code so it can be private. Also using value instead of a hard coded number
    // lets the integer be stated instead of it automatically infers the type.
    companion object {
        private const val ONE_MINUTE : Long = 60000
        private const val INTERVAL_CUT : Byte = 4
        val locationRequest : LocationRequest = LocationRequest.create().apply {
            interval = ONE_MINUTE
            fastestInterval = ONE_MINUTE/INTERVAL_CUT
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }
    }

}