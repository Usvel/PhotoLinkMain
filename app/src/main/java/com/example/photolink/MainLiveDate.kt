package com.example.photolink

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainLiveDate : ViewModel() {

    private val _namePlace: MutableLiveData<String> = MutableLiveData()
    val lastName: MutableLiveData<MutableList<String>> = MutableLiveData()
    val listURI: MutableLiveData<MutableList<Uri>> = MutableLiveData()

    val namePalace: LiveData<String> = _namePlace

    init {
        _namePlace.value = ""
        lastName.value = ArrayList()
    }

    fun addPlace(name: String) {
        _namePlace.value = "${_namePlace.value}/${name}"
        lastName.value?.add(name)
    }

    fun removePlace() {
        if (!lastName.value!!.isEmpty()) {
            _namePlace.value = _namePlace.value?.removeRange(_namePlace.value?.length!! - lastName.value?.last()!!.length - 1, _namePlace.value?.length!!)
            lastName.value?.removeLast()
        }
    }

    fun startMain(){
        _namePlace.value = ""
        lastName.value = ArrayList()
    }
}