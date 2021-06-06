package com.example.photolink.api

import android.content.Context
import android.provider.ContactsContract
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
import java.net.URI

class RequestApiImpl(
        val context: Context
) : RequestApi {

    companion object {
        val BASE_URL = "http://100.74.30.251:5000"
    }


    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
    private val client = OkHttpClient.Builder().addNetworkInterceptor(httpLoggingInterceptor).build()
   // private var retrofit = Retrofit.Builder().client(client).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
   private lateinit var retrofit:Retrofit
   // private var photoService = retrofit.create(PhotoService::class.java)
    private lateinit var photoService:PhotoService

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

    override fun updateRetrofit(baseURI: String){
        Log.d("Retrofit updated", baseURI)
        retrofit = Retrofit.Builder().client(client).baseUrl("http://$baseURI").addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        photoService = retrofit.create(PhotoService::class.java)
    }
}