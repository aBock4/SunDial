package com.sundial.v1001

import android.util.Log
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
import org.koin.core.component.getScopeId

class MainViewModel (private var twilightService : ITwilightService = TwilightService()) : ViewModel() {
    var twilight : MutableLiveData<List<Twilight>> = MutableLiveData<List<Twilight>>()
    var user : User? = null

    private lateinit var firestore : FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun fetchTwilight(){
        viewModelScope.launch{
            val innerTwilight = twilightService.fetchTwilight()
            twilight.postValue(innerTwilight)
        }
    }
    fun saveLocation(longitude : String, latitude: String) {
        user?.let { user ->
            val document =
                    firestore.collection("users").document(user.uid).collection("locations")
                        .document()
            var currentLongitude = longitude
            var currentLatitude = latitude
            currentLongitude = document.id
            var handle = document.set(currentLongitude)
            handle.addOnSuccessListener {
                Log.d("Firebase", "Document Saved")
            }
            handle.addOnFailureListener { Log.e("Firebase", "Save failed $it") }

            currentLatitude = document.id
            handle = document.set(currentLatitude)
            handle.addOnSuccessListener {
                Log.d("Firebase", "Document Saved")
            }
            handle.addOnFailureListener { Log.e("Firebase", "Save failed $it") }
        }
    }
}