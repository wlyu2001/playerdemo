package com.shishiapp.playerdemo.presentation.ui.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shishiapp.playerdemo.network.model.Video
import com.shishiapp.playerdemo.repository.Repository
import com.shishiapp.playerdemo.service.PlayerServiceCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    PlayerServiceCallback {

    val durationData = MutableLiveData<Long>()
    val positionData = MutableLiveData<Long>()

    val isPlayingData = MutableLiveData<Boolean>()
    val playerStateData = MutableLiveData<Int>()
    val repeatModeData = MutableLiveData<Int>()

    val currentVideoData = MutableLiveData<Video>()

    override fun setRepeatMode(repeatMode: Int) {
        repeatModeData.value = repeatMode
    }

    override fun setPlayerState(state: Int) {
        playerStateData.value = state
    }

    override fun setIsPlaying(isPlaying: Boolean) {
        isPlayingData.value = isPlaying
    }

    override fun setPosition(currentPosition: Long) {
        positionData.value = currentPosition
    }

    override fun setDuration(duration: Long) {
        durationData.value = duration
    }

    fun setCurrentVideo(key: String) {
        repository.getVideo(key, {
            currentVideoData.value = it
        }) { error ->

        }
    }


}