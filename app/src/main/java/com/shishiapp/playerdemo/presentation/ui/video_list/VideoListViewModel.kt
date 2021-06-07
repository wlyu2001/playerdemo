package com.shishiapp.playerdemo.presentation.ui.video_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishiapp.playerdemo.network.model.Video
import com.shishiapp.playerdemo.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(private val repository: Repository)
    : ViewModel() {

    val dataLoadingData = MutableLiveData<Boolean>().apply { value = false }

    val contentListData = MutableLiveData<List<Video>>()

    fun fetchContentList(sectionId: Int) {

        viewModelScope.launch {
            try {

                dataLoadingData.value = true
                repository.getVideoList(sectionId).collect {
                    contentListData.value = it
                }
            } catch (error: Error) {

            } finally {
                dataLoadingData.value = false
            }
        }
    }


}