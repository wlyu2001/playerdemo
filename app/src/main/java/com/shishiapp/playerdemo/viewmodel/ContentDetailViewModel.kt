package com.shishiapp.playerdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.model.Content
import com.shishiapp.playerdemo.model.ContentDetail
import com.shishiapp.playerdemo.model.ContentList
import com.shishiapp.playerdemo.network.PlexService

class ContentDetailViewModel : ViewModel() {

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val contentDetailLive = MutableLiveData<Content>()

    fun fetchContentDetail(key: String) {
        dataLoading.value = true

        PlexService.get(
            ContentDetail.url(key),
            ContentDetail::class.java,
            success = { contentDetail ->
                dataLoading.value = false
                contentDetailLive.value = contentDetail.content
            }) { error ->
            dataLoading.value = false
        }
    }


}