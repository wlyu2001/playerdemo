package com.shishiapp.playerdemo.presentation.ui.video_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.network.model.Video
import com.shishiapp.playerdemo.network.model.VideoDetail
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