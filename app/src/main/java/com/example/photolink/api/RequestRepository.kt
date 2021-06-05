package com.example.photolink.api

import com.example.photolink.Model.IteamPlace
import io.reactivex.Single
import okhttp3.ResponseBody
import java.io.File

class RequestRepository(
        private val api: RequestApi
) {
    fun loadListPlace(): Single<List<IteamPlace>> {
        return api.PlaceList()
    }

    fun loadListGson(): Single<List<IteamPlace>> {
        return api.LoadList()
    }

    fun saveFile(string: String, descriptioin: String, file: List<File>): Single<ResponseBody> {
        return api.saveImage(string, descriptioin, file)
    }
}