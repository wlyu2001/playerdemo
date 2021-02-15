package com.shishiapp.playerdemo.service

interface OnPlayerServiceCallback {

    fun setBufferingData(isBuffering: Boolean)

    // this is actually playWhenReady.
    fun setIsPlaying(isPlaying: Boolean)

    fun stopService()

    fun setPosition(currentPosition: Long)

    fun setDuration(duration: Long)
}