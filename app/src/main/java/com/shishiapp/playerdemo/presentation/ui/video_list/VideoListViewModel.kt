package com.shishiapp.playerdemo.presentation.ui.video_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.network.model.Video
import com.shishiapp.playerdemo.network.model.VideoList
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

        repository.get(
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