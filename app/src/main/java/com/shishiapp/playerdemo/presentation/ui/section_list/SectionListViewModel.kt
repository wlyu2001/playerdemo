package com.shishiapp.playerdemo.presentation.ui.section_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.network.model.Section
import com.shishiapp.playerdemo.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SectionListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val dataLoading = MutableLiveData<Boolean>().apply { value = false }

    val sectionListLive = MutableLiveData<List<Section>>()

    fun fetchSectionList() {
        dataLoading.value = true

        repository.getSectionList({
            dataLoading.value = false
            sectionListLive.value = it
        }){
            dataLoading.value = false
        }
    }


}