package com.shishiapp.playerdemo.di

import com.shishiapp.playerdemo.network.PlexService
import com.shishiapp.playerdemo.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providePlexService(): PlexService {
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
        val baseUrl = HttpUrl.parse(Constants.baseUrl)!!
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(client)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .baseUrl(baseUrl)
            .build()


        return retrofit.create(PlexService::class.java)
    }

    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()

        return Realm.getInstance(config)
    }
}