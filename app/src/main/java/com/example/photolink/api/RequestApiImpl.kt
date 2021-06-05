package com.example.photolink.api

import android.content.Context
import android.util.Log
import com.example.photolink.Model.IteamPlace
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.io.InputStream

class RequestApiImpl(
        val context: Context
) : RequestApi {

    companion object {
        val BASE_URL = "http://100.74.30.251:5000"
    }


    val httpLoggingInterceptor = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
    val client = OkHttpClient.Builder().addNetworkInterceptor(httpLoggingInterceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val photoService = retrofit.create(PhotoService::class.java)

    override fun PlaceList(): Single<List<IteamPlace>> {
        val myJson: String = inputStreamToString(context.assets.open("json.json")).toString()
        val postType = object : TypeToken<List<IteamPlace>>() {}.type
        val enums: List<IteamPlace> = Gson().fromJson(myJson, postType)

        // val list: List<Iteam> = Gson().fromJson(myJson, postType)
        // Log.d("New", list.toString())
        return Single.just(enums)
    }

    override fun LoadList(): Single<List<IteamPlace>> {

        return photoService.getUserInfo()
        //return Single.just(listOf())
    }

    override fun saveImage(string: String, descriptioin: String, files: List<File>): Single<ResponseBody> {
        val descriptionPart = RequestBody.create(MultipartBody.FORM, descriptioin)

        val arrayList = ArrayList<MultipartBody.Part>()
        files.forEachIndexed() { index, file ->
            Log.d("File", file.name)
            val requstFile = RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    file
            )
            Log.d("Bo", file.name)
            val fileMut = MultipartBody.Part.createFormData("photo$index", filename = file.name, requstFile)
            arrayList.add(fileMut)
        }

        return photoService.uploadPhoto(string, descriptionPart, arrayList)
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