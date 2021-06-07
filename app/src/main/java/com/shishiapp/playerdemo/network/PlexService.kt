package com.shishiapp.playerdemo.network

import com.shishiapp.playerdemo.network.model.SectionList
import com.shishiapp.playerdemo.network.model.Video
import com.shishiapp.playerdemo.network.model.VideoDetail
import com.shishiapp.playerdemo.network.model.VideoList
import retrofit2.http.GET
import retrofit2.http.Path


interface PlexService {
    @GET("{path}")
    suspend fun getSectionList(@Path("path", encoded = true) path: String): SectionList

    @GET("{path}")
    suspend fun getContentList(@Path("path", encoded = true) path: String): VideoList

    @GET("{path}")
    suspend fun getContentDetail(@Path("path", encoded = true) path: String): VideoDetail


    @GET("/")
    suspend fun checkToken(@Path("path", encoded = true) path: String): Video

}