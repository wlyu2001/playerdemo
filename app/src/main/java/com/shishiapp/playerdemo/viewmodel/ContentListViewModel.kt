package com.shishiapp.playerdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.model.Content
import com.shishiapp.playerdemo.model.ContentList
import com.shishiapp.playerdemo.network.PlexService

class ContentListViewModel : ViewModel() {

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val contentListLive = MutableLiveData<List<Content>>()

    fun fetchContentList(sectionId: Int) {
        dataLoading.value = true

        PlexService.get(
            ContentList.url(sectionId),
            ContentList::class.java,
            success = { contentList ->
                dataLoading.value = false
                contentListLive.value = contentList.contents
            }) { error ->
            dataLoading.value = false
        }
    }


}