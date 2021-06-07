package com.shishiapp.playerdemo.repository

import com.shishiapp.playerdemo.network.model.Section
import com.shishiapp.playerdemo.network.model.Video
import kotlinx.coroutines.flow.Flow

class FakeRepositoryImp : Repository {

    private val videos = mutableListOf<Video>()

    fun initializeVideos() {

        videos.add(Video().apply {
            key = "1"
            title = "title 1"
        })
    }

    var shouldReturnError = false

    override suspend fun getVideo(key: String): Flow<Video> {
        TODO("Not yet implemented")
    }

    override suspend fun getSection(key: Int): Flow<Section> {
        TODO("Not yet implemented")
    }

    override suspend fun getSectionList(): Flow<List<Section>> {
        TODO("Not yet implemented")
    }

    override suspend fun getVideoList(sectionId: Int): Flow<List<Video>> {
        TODO("Not yet implemented")
    }
}