package com.shishiapp.playerdemo.presentation.ui.section_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shishiapp.playerdemo.network.model.Section
import com.shishiapp.playerdemo.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Error
import javax.inject.Inject

@HiltViewModel
class SectionListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val dataLoadingData = MutableLiveData<Boolean>().apply { value = false }

    val sectionListData = MutableLiveData<List<Section>>()

    fun fetchSectionList() {

        viewModelScope.launch {
            try {
                dataLoadingData.value = true
                repository.getSectionList().collect {
                    sectionListData.postValue(it)
                }
            } catch (error: Error) {
                // show error
            } finally {
                dataLoadingData.value = false
            }
        }
    }


}