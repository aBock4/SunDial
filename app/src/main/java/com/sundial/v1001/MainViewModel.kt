package com.sundial.v1001

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.sundial.v1001.dto.User
import kotlinx.coroutines.launch
import com.sundial.v1001.dto.Location
import com.sundial.v1001.service.ICityService


class MainViewModel(private var cityService: ICityService) : ViewModel() {
    val newLocation = "New Location"
    var cities = cityService.getLocalCityDAO().getAllCities()
    var locations: MutableLiveData<List<Location>> = MutableLiveData<List<Location>>()
    var selectedLocation by mutableStateOf(Location())
    var user: User? = null

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun listenToLocations() {
        user?.let { user ->
            firestore.collection("users").document(user.uid).collection("locations")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("Listen failed", e)
                        return@addSnapshotListener
                    }
                    snapshot?.let {
                        val allLocations = ArrayList<Location>()
                        allLocations.add(
                            Location(
                                longitude = "",
                                locationName = newLocation,
                                latitude = "",
                                sunrise = "",
                                sunset = ""
                            )
                        )
                        val documents = snapshot.documents
                        documents.forEach { document ->
                            val location = document.toObject(Location::class.java)
                            location?.let { loca ->
                                allLocations.add(loca)
                            }
                        }
                        locations.value = allLocations
                    }
                }
        }
    }

    fun fetchCities() {
        viewModelScope.launch {
            cityService.fetchCities()
        }
    }

    fun saveLocation() {
        user?.let { user ->
            val document =
                if (selectedLocation.locationId.isEmpty()) {
                    firestore.collection("users").document(user.uid).collection("locations")
                        .document()
                } else {
                    firestore.collection("users").document(user.uid).collection("locations")
                        .document(selectedLocation.locationId)
                }
            selectedLocation.locationId = document.id
            val handle = document.set(selectedLocation)
            handle.addOnSuccessListener { Log.d("Firebase", "Location Saved") }
            handle.addOnFailureListener { Log.e("Firebase", "Save failed $it") }
        }
    }

    fun deleteLocation(location: Location) {
        user?.let { user ->
            val locationCollection =
                firestore.collection("users").document(user.uid).collection("locations")
            locationCollection.document(selectedLocation.locationId).delete()
                .addOnSuccessListener { Log.d("Firebase", "Location Deleted") }
                .addOnFailureListener { Log.e("Firebase", "Delete failed $location") }
        }
        val blankLocation = Location()
        selectedLocation = blankLocation
    }

    fun saveUser() {
        user?.let { user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
            handle.addOnFailureListener { Log.e("Firebase", "Save failed $it") }
        }
    }
}