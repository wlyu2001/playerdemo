package com.shishiapp.playerdemo.repository

import com.shishiapp.playerdemo.network.model.Section
import com.shishiapp.playerdemo.network.model.Video
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getVideo(key: String): Flow<Video?>

    suspend fun getSection(key: Int): Flow<Section?>

    suspend fun getSectionList(): Flow<List<Section>>

    suspend fun getVideoList(sectionId: Int): Flow<List<Video>>
}