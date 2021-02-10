package com.shishiapp.playerdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.model.Section
import com.shishiapp.playerdemo.model.SectionList
import com.shishiapp.playerdemo.network.PlexService

class SectionListViewModel : ViewModel() {

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val sectionListLive = MutableLiveData<List<Section>>()


    fun fetchSectionList() {
        dataLoading.value = true

        PlexService.get(SectionList.url(), SectionList::class.java, success = { sectionList ->
            dataLoading.value = false
            sectionListLive.value = sectionList.sections
        }) { error ->
            dataLoading.value = false
        }
    }


}