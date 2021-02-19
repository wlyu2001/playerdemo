package com.shishiapp.playerdemo.presentation.ui.section_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.network.model.Section
import com.shishiapp.playerdemo.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SectionListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val dataLoadingData = MutableLiveData<Boolean>().apply { value = false }

    val sectionListData = MutableLiveData<List<Section>>()

    fun fetchSectionList() {
        dataLoadingData.value = true

        repository.getSectionList({
            dataLoadingData.value = false
            sectionListData.value = it
        }){
            dataLoadingData.value = false
        }
    }


}