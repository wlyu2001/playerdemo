package com.shishiapp.playerdemo.di

import com.shishiapp.playerdemo.network.PlexService
import com.shishiapp.playerdemo.repository.Repository
import com.shishiapp.playerdemo.repository.RepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(plexService: PlexService, realm: Realm): Repository {

        return RepositoryImp(plexService, realm)
    }
}