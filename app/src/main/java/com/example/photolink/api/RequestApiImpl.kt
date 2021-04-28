package com.example.photolink.api

import android.content.Context
import com.example.photolink.Model.IteamPlace
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import java.io.IOException
import java.io.InputStream

class RequestApiImpl(
        val context: Context
) : RequestApi {

    override fun PlaceList(): Single<List<IteamPlace>> {
        val myJson: String = inputStreamToString(context.assets.open("place_list.json")).toString()
        val postType = object : TypeToken<List<IteamPlace>>() {}.type
        val enums: List<IteamPlace> = Gson().fromJson(myJson, postType)

        // val list: List<Iteam> = Gson().fromJson(myJson, postType)
        // Log.d("New", list.toString())
        return Single.just(enums)
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