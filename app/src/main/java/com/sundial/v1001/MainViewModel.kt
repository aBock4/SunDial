package com.sundial.v1001

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sundial.v1001.dto.Twilight
import com.sundial.v1001.dto.User
import com.sundial.v1001.service.ITwilightService
import com.sundial.v1001.service.TwilightService
import kotlinx.coroutines.launch

/**
 * This view model fetches data from an ITwilightService instance and provides it as
 * MutableLiveData
 *
 * @property twilightService Instance of ITwilightService used to fetch data
 * @property twilight MutableLiveData object that holds a list of Twilight objects
 * @property user Current user
 */
class MainViewModel (private var twilightService : ITwilightService = TwilightService()) : ViewModel() {
    var twilight : MutableLiveData<List<Twilight>> = MutableLiveData<List<Twilight>>()
    var user : User? = null

    fun fetchTwilight(){
        viewModelScope.launch{
            val innerTwilight = twilightService.fetchTwilight()
            twilight.postValue(innerTwilight)
        }
    }
}