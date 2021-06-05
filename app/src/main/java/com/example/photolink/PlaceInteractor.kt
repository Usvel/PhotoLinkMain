package com.example.photolink

import com.example.photolink.Model.IteamPlace

interface PlaceInteractor {
    fun onClickPlace(list: List<IteamPlace>, type: Boolean, name: String)
    fun onRefreshPlace()
}