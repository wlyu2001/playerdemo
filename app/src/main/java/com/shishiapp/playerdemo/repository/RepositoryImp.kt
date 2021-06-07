package com.shishiapp.playerdemo.repository

import com.shishiapp.playerdemo.network.PlexService
import com.shishiapp.playerdemo.network.model.*
import io.realm.Realm
import io.realm.RealmModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryImp(private val plexService: PlexService, private val realm: Realm) : Repository {

    private suspend fun <T : RealmModel> fetch(path: String, type: Class<T>): T {
        try {
            val response = when (type) {
                SectionList::class.java ->
                    plexService.getSectionList(path)
                VideoList::class.java ->
                    plexService.getContentList(path)
                VideoDetail::class.java ->
                    plexService.getContentDetail(path)

                else -> null
            }


            realm.beginTransaction()
            realm.insertOrUpdate(response)
            realm.commitTransaction()
            return response as T
        } catch (error: Error) {
            throw error
        }
    }

    override suspend fun getVideo(key: String): Flow<Video> = flow {

        realm.where(Video::class.java).equalTo("key", key).findFirst()?.let { emit(it) }

        fetch(VideoDetail.url(key), VideoDetail::class.java).video?.let { emit(it) }
    }

    override suspend fun getSection(key: Int): Flow<Section> = flow {
        realm.where(Section::class.java).equalTo("key", key).findFirst()?.let { emit(it) }

        fetch(Section.url(sectionKey = key), Section::class.java)?.let{ emit(it)}
    }

    override suspend fun getSectionList(): Flow<List<Section>> = flow {
        realm.where(SectionList::class.java).findFirst()?.let {
            emit(it.sections)
        }

        emit(fetch(SectionList.url(), SectionList::class.java).sections)
    }

    override suspend fun getVideoList(sectionId: Int): Flow<List<Video>> = flow {
        realm.where(VideoList::class.java).equalTo("sectionId", sectionId).findFirst()?.let {
            emit(it.videos)
        }

        emit(fetch(VideoList.url(sectionId), VideoList::class.java).videos)
    }
}