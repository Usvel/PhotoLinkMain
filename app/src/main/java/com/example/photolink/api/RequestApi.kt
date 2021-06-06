package com.example.photolink.api

import com.example.photolink.Model.IteamPlace
import io.reactivex.Single
import okhttp3.ResponseBody
import java.io.File

interface RequestApi {
    fun PlaceList(): Single<List<IteamPlace>>
    fun LoadList(): Single<List<IteamPlace>>
    fun saveImage(string: String, descriptioin: String, files: List<File>): Single<ResponseBody>
    fun updateRetrofit(baseURI: String)
}