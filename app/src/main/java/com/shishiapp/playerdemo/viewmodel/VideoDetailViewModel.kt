package com.shishiapp.playerdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.model.VideoDetail
import com.shishiapp.playerdemo.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel @Inject constructor(private val repository: Repository)
    : ViewModel() {

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val contentDetailLive = MutableLiveData<Video>()


    fun fetchContentDetail(key: String) {
        dataLoading.value = true

        repository.get(
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