package com.example.photolink.api

import com.example.photolink.Model.IteamPlace
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface PhotoService {
    @GET("./dir/root")
    fun getUserInfo(): Single<List<IteamPlace>>

    @Multipart
    @POST("/imageset{id}")
    fun uploadPhoto(
            @Path("id") string: String,
            @Part("description") description : RequestBody,
            @Part photo: List<MultipartBody.Part>
    ): Single<ResponseBody>
}