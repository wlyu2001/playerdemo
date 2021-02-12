package com.shishiapp.playerdemo.network

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import com.shishiapp.playerdemo.model.Content
import com.shishiapp.playerdemo.model.ContentDetail
import com.shishiapp.playerdemo.model.ContentList
import com.shishiapp.playerdemo.model.SectionList
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmModel
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
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
    fun getContentList(@Path("path", encoded = true) path: String): Observable<ContentList>

    @GET("{path}")
    fun getContentDetail(@Path("path", encoded = true) path: String): Observable<ContentDetail>


    @GET("/")
    fun checkToken(@Path("path", encoded = true) path: String): Observable<Content>

}


object PlexService {


    lateinit var plexApi: PlexAPI
    private var realm = Realm.getDefaultInstance()
    private var baseUrl = HttpUrl.parse("http://10.0.2.2:32400")!!
    private var token = ""

    fun getImageUrl(path: String): String {
        return baseUrl.newBuilder().addEncodedPathSegments(path.removePrefix("/"))
            .addQueryParameter("X-Plex-Token", this.token).build().toString()
    }

    fun checkToken(token: String, completion: (Boolean) -> Unit) {

        val client = OkHttpClient()

        val url = baseUrl.newBuilder()
            .addQueryParameter("X-Plex-Token", token)
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

    fun initService(token: String) {

        this.token = token
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                var request = chain.request()

                val url =
                    request.url().newBuilder()
                        .addQueryParameter("X-Plex-Token", token)
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
            ContentList::class.java ->
                plexApi.getContentList(path)
            ContentDetail::class.java ->
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