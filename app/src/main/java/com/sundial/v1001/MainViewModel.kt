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
import com.sundial.v1001.dto.Twilight
import com.sundial.v1001.dto.User
import com.sundial.v1001.service.ITwilightService
import com.sundial.v1001.service.TwilightService
import kotlinx.coroutines.launch
import com.google.firebase.storage.FirebaseStorage
import com.sundial.v1001.dto.Location
import com.sundial.v1001.dto.LocationDetails
import com.sundial.v1001.service.locationService
import org.koin.core.component.getScopeId

class MainViewModel (private var twilightService : ITwilightService = TwilightService()) : ViewModel() {
    internal val NEW_LOCATION = "New Location"
    var twilight : MutableLiveData<List<Twilight>> = MutableLiveData<List<Twilight>>()
    var locations: MutableLiveData<List<Location>> = MutableLiveData<List<Location>>()
    var selectedLocation by mutableStateOf(Location())
    var user : User? = null
    var location : Location? =null

    private lateinit var firestore : FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToLocations()
    }

    private fun listenToLocations() {
        user?.let {
            user ->
            firestore.collection("users").document(user.uid).collection("locations").addSnapshotListener {
                snapshot, e ->
                if (e != null) {
                    Log.w("Listen failed", e)
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val allLocations = ArrayList<Location>()
                    allLocations.add(Location(locationName = NEW_LOCATION))
                    val documents = snapshot.documents
                    documents.forEach {
                        val location = it.toObject(Location::class.java)
                        location?.let{
                            allLocations.add(it)
                        }
                    }
                    locations.value = allLocations
                }
            }
        }
    }

    fun fetchTwilight(){
        viewModelScope.launch{
            val innerTwilight = twilightService.fetchTwilight()
            twilight.postValue(innerTwilight)
        }
    }

    /*fun fetchLocations() {
        viewModelScope.launch {
            locationService.fetchLocations()
        }
    }*/
    fun saveLocation() {
        user?.let {
                user ->
               val document =
                   if (selectedLocation.locationId == null || selectedLocation.locationId.isEmpty()) {
                       firestore.collection("users").document(user.uid).collection("locations").document()
                   } else {
                       firestore.collection("users").document(user.uid).collection("locations").document(selectedLocation.locationId)
                   }
                selectedLocation.locationId = document.id
                val handle = document.set(selectedLocation)
                handle.addOnSuccessListener { Log.d("Firebase", "Location Saved") }
                handle.addOnFailureListener { Log.e("Firebase", "Save failed $it") }
        }
    }
    fun saveUser() {
        user?.let{
                user ->
            val handle = firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
            handle.addOnFailureListener { Log.e("Firebase", "Save failed $it") }
        }
    }
}