package com.example.photolink.api

import android.content.Context
import com.example.photolink.Model.Request
import com.google.gson.Gson
import io.reactivex.Single
import java.io.IOException
import java.io.InputStream

class RequestApiImpl(
        val context: Context
) : RequestApi {

    override fun PlaceList(): Single<Request> {
        val myJson : String = inputStreamToString(context.assets.open("place_list.json")).toString()
        //val postType = object : TypeToken<List<Post>>() {}.type
        val list: Request = Gson().fromJson<Request>(myJson, Request::class.java)
        return Single.just(list)
    }

    override fun RowList(): Single<Request> {
        val myJson : String = inputStreamToString(context.assets.open("row_list.json")).toString()
        //val postType = object : TypeToken<List<Post>>() {}.type
        val list: Request = Gson().fromJson<Request>(myJson, Request::class.java)
        return Single.just(list)
    }

    fun inputStreamToString(inputStream: InputStream): String? {
        return try {
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            String(bytes)
        } catch (e: IOException) {
            null
        }
    }
}