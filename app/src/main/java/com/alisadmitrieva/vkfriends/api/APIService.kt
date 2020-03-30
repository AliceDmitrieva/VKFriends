package com.alisadmitrieva.vkfriends.api

import com.alisadmitrieva.vkfriends.BuildConfig
import com.alisadmitrieva.vkfriends.models.ApiResponseList
import com.alisadmitrieva.vkfriends.models.ApiResponseObject
import com.alisadmitrieva.vkfriends.models.FriendInfo
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("friends.get")
    fun getFriends(
        @Query("order") order: String,
        @Query("fields") fields: String,
        @Query("offset") offset: Int,
        @Query("count") count: Int,
        @Query("v") v: String,
        @Query("access_token") token: String?
    ): Observable<ApiResponseObject<ApiResponseList<FriendInfo>>>


    companion object {
        fun getService(): APIService {

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(APIService::class.java)
        }
    }

}
