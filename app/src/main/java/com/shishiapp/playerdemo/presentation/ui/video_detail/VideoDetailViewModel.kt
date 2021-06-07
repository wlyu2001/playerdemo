package com.shishiapp.playerdemo.presentation.ui.video_detail

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
class VideoDetailViewModel @Inject constructor(private val repository: Repository)
    : ViewModel() {

    val dataLoadingData = MutableLiveData<Boolean>().apply { value = false }

    val contentDetailData = MutableLiveData<Video>()


    fun fetchContentDetail(key: String) {

        viewModelScope.launch {

            try {
                dataLoadingData.value = true
                repository.getVideo(key).collect {
                    contentDetailData.value = it
                }
            } catch (error: Error) {
                //show error
            } finally {
                dataLoadingData.value = false
            }
        }

    }


}