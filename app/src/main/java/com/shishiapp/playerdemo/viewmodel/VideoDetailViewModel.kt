package com.shishiapp.playerdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.model.VideoDetail
import com.shishiapp.playerdemo.network.PlexService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel @Inject constructor(private val plexService: PlexService)
    : ViewModel() {

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val contentDetailLive = MutableLiveData<Video>()


    fun fetchContentDetail(key: String) {
        dataLoading.value = true

        plexService.get(
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