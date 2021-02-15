package com.shishiapp.playerdemo.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.network.PlexService


class PlayerService : Service(), Player.EventListener {
    private lateinit var mExoPlayer: SimpleExoPlayer
    private val mBinder = PlayerServiceBinder()
    private var mCallback: OnPlayerServiceCallback? = null

    private val handler = Handler()
    private var durationSet = false
    private var playing = false

    override fun onCreate() {
        super.onCreate()

        mExoPlayer = SimpleExoPlayer.Builder(this).build()

        mExoPlayer.addListener(this)
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }


    inner class PlayerServiceBinder : Binder() {
        val service: PlayerService
            get() = this@PlayerService
        val player: ExoPlayer
            get() = mExoPlayer
    }

    fun addListener(callback: OnPlayerServiceCallback) {
        mCallback = callback
    }

    fun removeListener() {
        mCallback = null
    }


    fun play() {
        mExoPlayer.playWhenReady = true
    }

    fun seekTo(position: Long) {
        mExoPlayer.seekTo(position)
    }

    fun pause() {
        mExoPlayer.playWhenReady = false
    }


    override fun onIsPlayingChanged(isPlaying: Boolean) {
        playing = isPlaying
        if (isPlaying) {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    if (playing) {
                        mCallback?.setPosition(mExoPlayer.currentPosition)
                        handler.postDelayed(this, 100)
                    }
                }
            }, 100)
        }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        mCallback?.setRepeatMode(repeatMode)
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        mCallback?.setIsPlaying(playWhenReady)
    }


    override fun onPlaybackStateChanged(state: Int) {

        mCallback?.setPlayerState(state)

        when (state) {
            Player.STATE_READY -> {
                if (!durationSet) {
                    durationSet = true
                    mCallback?.setDuration(mExoPlayer.duration)
                }
            }

            Player.STATE_ENDED -> {
                mExoPlayer.seekTo(0)
                mExoPlayer.playWhenReady = false
                mCallback?.setPosition(0)
            }

            else -> {

            }

        }


//        mNotificationManager?.generateNotification()
    }


    fun loadVideo(video: Video?) {
        val part = video?.media?.get(0)?.parts?.get(0)
        if (part != null) {
            val mediaItem = MediaItem.fromUri(PlexService.getMediaUrl(part.key))

            mExoPlayer.addMediaItem(mediaItem)


        }
        mExoPlayer.prepare()
        mExoPlayer.repeatMode = Player.REPEAT_MODE_OFF
        mCallback?.setRepeatMode(Player.REPEAT_MODE_OFF)

    }

    fun setRepeatMode(repeatMode: Int) {
        mExoPlayer.repeatMode = repeatMode
    }


}