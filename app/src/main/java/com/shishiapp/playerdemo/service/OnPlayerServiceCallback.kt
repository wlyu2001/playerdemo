package com.shishiapp.playerdemo.service

interface OnPlayerServiceCallback {


    fun setRepeatMode(repeatMode: Int)

    fun setPlayerState(state: Int)

    // this is actually playWhenReady.
    fun setIsPlaying(isPlaying: Boolean)

    fun stopService()

    fun setPosition(currentPosition: Long)

    fun setDuration(duration: Long)
}