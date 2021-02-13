package com.shishiapp.playerdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.model.VideoDetail
import com.shishiapp.playerdemo.network.PlexService

class VideoDetailViewModel : ViewModel() {

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val contentDetailLive = MutableLiveData<Video>()

    fun fetchContentDetail(key: String) {
        dataLoading.value = true

        PlexService.get(
            VideoDetail.url(key),
            VideoDetail::class.java,
            success = { contentDetail ->
                dataLoading.value = false
                contentDetailLive.value = contentDetail.video
            }) { error ->
            dataLoading.value = false
        }
    }


}