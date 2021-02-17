package com.shishiapp.playerdemo.network

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import com.shishiapp.playerdemo.model.SectionList
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.model.VideoDetail
import com.shishiapp.playerdemo.model.VideoList
import com.shishiapp.playerdemo.util.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmModel
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException


interface PlexAPI {
    @GET("{path}")
    fun getSectionList(@Path("path", encoded = true) path: String): Observable<SectionList>

    @GET("{path}")
    fun getContentList(@Path("path", encoded = true) path: String): Observable<VideoList>

    @GET("{path}")
    fun getContentDetail(@Path("path", encoded = true) path: String): Observable<VideoDetail>


    @GET("/")
    fun checkToken(@Path("path", encoded = true) path: String): Observable<Video>

}

class PlexService constructor(private val realm: Realm) {

    lateinit var plexApi: PlexAPI

    private val baseUrl = HttpUrl.parse(Constants.baseUrl)!!

    fun connect(completion: (Boolean) -> Unit) {

        val client = OkHttpClient()

        val url = baseUrl.newBuilder()
            .addQueryParameter("X-Plex-Token", Constants.token)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val mainHandler = Handler(Looper.getMainLooper())

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    completion(false)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    mainHandler.post {
                        completion(response.isSuccessful)
                    }
                }
            }
        })
    }

    fun initService() {

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request = chain.request()

                val url =
                    request.url().newBuilder()
                        .addQueryParameter("X-Plex-Token", Constants.token)
                        .build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(client)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .baseUrl(baseUrl)
            .build()


        plexApi = retrofit.create(PlexAPI::class.java)

    }

    @SuppressLint("CheckResult")
    internal fun <T : RealmModel> get(
        path: String,
        type: Class<T>,
        success: (T) -> Unit,
        fail: (Error) -> Unit
    ) {

        val observable = when (type) {
            SectionList::class.java ->
                plexApi.getSectionList(path)
            VideoList::class.java ->
                plexApi.getContentList(path)
            VideoDetail::class.java ->
                plexApi.getContentDetail(path)

            else -> null
        }

        observable?.subscribeOn(Schedulers.newThread())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ response ->
                realm.beginTransaction()
                realm.insertOrUpdate(response)
                realm.commitTransaction()
                success(response as T)
            },
                { error ->
                    fail(Error(error))
                })
    }
}