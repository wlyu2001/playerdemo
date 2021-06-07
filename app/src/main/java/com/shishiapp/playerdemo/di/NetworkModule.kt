package com.shishiapp.playerdemo.di

import android.content.Context
import com.shishiapp.playerdemo.network.PlexService
import com.shishiapp.playerdemo.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
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
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .baseUrl(baseUrl)
            .client(client)
            .build()


        return retrofit.create(PlexService::class.java)
    }

    @Singleton
    @Provides
    fun provideRealm(@ApplicationContext appContext: Context): Realm {

        Realm.init(appContext)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()

        return Realm.getInstance(config)
    }
}