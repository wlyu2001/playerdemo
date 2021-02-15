package com.shishiapp.playerdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.service.OnPlayerServiceCallback
import io.realm.Realm
import io.realm.kotlin.where

class PlayerViewModel : ViewModel(), OnPlayerServiceCallback {

    private var realm = Realm.getDefaultInstance()

    val durationData = MutableLiveData<Long>()
    val positionData = MutableLiveData<Long>()

    val isBufferingData = MutableLiveData<Boolean>()
    val isPlayingData = MutableLiveData<Boolean>()

    val currentVideo = MutableLiveData<Video>()

    override fun setBufferingData(isBuffering: Boolean) {

    }

    override fun setIsPlaying(isPlaying: Boolean) {
        isPlayingData.value = isPlaying
    }

    override fun stopService() {

    }

    override fun setPosition(currentPosition: Long) {
        positionData.value = currentPosition
    }

    override fun setDuration(duration: Long) {
        durationData.value = duration
    }

    fun setCurrentVideo(key: String?) {
        currentVideo.value = realm.where<Video>().equalTo("key", key).findFirst()
    }


}