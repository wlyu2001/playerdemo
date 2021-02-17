package com.shishiapp.playerdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.model.Section
import com.shishiapp.playerdemo.model.SectionList
import com.shishiapp.playerdemo.network.PlexService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SectionListViewModel @Inject constructor(private val plexService: PlexService) : ViewModel() {

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val sectionListLive = MutableLiveData<List<Section>>()


    fun fetchSectionList() {
        dataLoading.value = true

        plexService.get(SectionList.url(), SectionList::class.java, success = { sectionList ->
            dataLoading.value = false
            sectionListLive.value = sectionList.sections
        }) { error ->
            dataLoading.value = false
        }
    }


}