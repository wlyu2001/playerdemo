package com.shishiapp.playerdemo.presentation.ui.video_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.network.model.Video
import com.shishiapp.playerdemo.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(private val repository: Repository)
    : ViewModel() {

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val contentListLive = MutableLiveData<List<Video>>()

    fun fetchContentList(sectionId: Int) {
        dataLoading.value = true

        repository.getVideoList(sectionId, {
            dataLoading.value = false
            contentListLive.value = it
        }) {
            dataLoading.value = false
        }
    }


}