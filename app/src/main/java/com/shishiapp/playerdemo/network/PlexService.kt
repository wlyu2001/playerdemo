package com.shishiapp.playerdemo.network

import com.shishiapp.playerdemo.network.model.SectionList
import com.shishiapp.playerdemo.network.model.Video
import com.shishiapp.playerdemo.network.model.VideoDetail
import com.shishiapp.playerdemo.network.model.VideoList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface PlexService {
    @GET("{path}")
    fun getSectionList(@Path("path", encoded = true) path: String): Observable<SectionList>

    @GET("{path}")
    fun getContentList(@Path("path", encoded = true) path: String): Observable<VideoList>

    @GET("{path}")
    fun getContentDetail(@Path("path", encoded = true) path: String): Observable<VideoDetail>


    @GET("/")
    fun checkToken(@Path("path", encoded = true) path: String): Observable<Video>

}