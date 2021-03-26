package com.example.photolink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photolink.Model.Row

class RowViewModel : ViewModel() {
    private val _rowList : MutableLiveData<List<Row>> = MutableLiveData()

    val placeList: LiveData<List<Row>> = _rowList

    init {
        _rowList.value = PlaceData.getRow()
    }

    fun updateListPlace(listRow :List<Row>) {
        _rowList.value = listRow
    }
}