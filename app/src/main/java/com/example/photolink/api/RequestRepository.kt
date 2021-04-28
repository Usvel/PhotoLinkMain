package com.example.photolink.api

import com.example.photolink.Model.IteamPlace
import io.reactivex.Single

class RequestRepository(
        private val api: RequestApi
) {
    fun loadListPlace(): Single<List<IteamPlace>> {
        return api.PlaceList()
    }
}