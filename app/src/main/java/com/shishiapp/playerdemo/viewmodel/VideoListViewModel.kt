package com.shishiapp.playerdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.model.VideoList
import com.shishiapp.playerdemo.network.PlexService

class VideoListViewModel : ViewModel() {

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val contentListLive = MutableLiveData<List<Video>>()

    fun fetchContentList(sectionId: Int) {
        dataLoading.value = true

        PlexService.get(
            VideoList.url(sectionId),
            VideoList::class.java,
            success = { contentList ->
                dataLoading.value = false
                contentListLive.value = contentList.videos
            }) { error ->
            dataLoading.value = false
        }
    }


}