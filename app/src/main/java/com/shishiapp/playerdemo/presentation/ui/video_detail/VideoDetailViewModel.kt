package com.shishiapp.playerdemo.presentation.ui.video_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.network.model.Video
import com.shishiapp.playerdemo.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel @Inject constructor(private val repository: Repository)
    : ViewModel() {

    val dataLoadingData = MutableLiveData<Boolean>().apply { value = false }

    val contentDetailData = MutableLiveData<Video>()


    fun fetchContentDetail(key: String) {
        dataLoadingData.value = true

        repository.getVideo(key, {
            dataLoadingData.value = false
            contentDetailData.value = it
        }) {
            dataLoadingData.value = false
        }
    }


}