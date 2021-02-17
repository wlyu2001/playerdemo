package com.shishiapp.playerdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.model.VideoList
import com.shishiapp.playerdemo.network.PlexService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(private val plexService: PlexService)
    : ViewModel() {

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val contentListLive = MutableLiveData<List<Video>>()

    fun fetchContentList(sectionId: Int) {
        dataLoading.value = true

        plexService.get(
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