package com.example.photolink.api

import com.example.photolink.Model.Request
import io.reactivex.Single

class RequestRepository(
        private val api: RequestApi
) {
    fun loadListPlace() : Single<Request> {
        return api.PlaceList()
    }

    fun loadListRow() : Single<Request> {
        return api.RowList()
    }
}