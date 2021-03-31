package com.example.photolink.api

import com.example.photolink.Model.Request
import io.reactivex.Single

interface RequestApi {
    fun PlaceList(): Single<Request>

    fun RowList(): Single<Request>
}