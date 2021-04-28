package com.example.photolink.api

import com.example.photolink.Model.IteamPlace
import io.reactivex.Single

interface RequestApi {
    fun PlaceList(): Single<List<IteamPlace>>
}