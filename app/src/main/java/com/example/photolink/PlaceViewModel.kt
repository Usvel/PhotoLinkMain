package com.example.photolink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photolink.Model.Place

class PlaceViewModel : ViewModel() {
    private val _placeList : MutableLiveData<List<Place>> = MutableLiveData()

    val placeList: LiveData<List<Place>> = _placeList

    init {
        _placeList.value = listOf()
    }

    fun updateListPlace() {
        _placeList.value = PlaceData.getPlace()
    }
}