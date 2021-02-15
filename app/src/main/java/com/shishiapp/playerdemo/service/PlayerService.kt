package com.shishiapp.playerdemo.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.shishiapp.playerdemo.R
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.network.PlexService
import com.shishiapp.playerdemo.playerIntent
import com.squareup.picasso.Picasso


private const val PLAYBACK_CHANNEL_ID = "playback_channel"
private const val PLAYBACK_NOTIFICATION_ID = 1
private const val MEDIA_SESSION_TAG = "media_session"

class PlayerService : Service(), Player.EventListener {
    private lateinit var player: SimpleExoPlayer
    private val binder = PlayerServiceBinder()
    private var callback: OnPlayerServiceCallback? = null
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private val handler = Handler()
    private var durationSet = false
    private var playing = false

    private var video: Video? = null


    override fun onCreate() {
        super.onCreate()

        player = SimpleExoPlayer.Builder(this).build()
        player.addListener(this)

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            applicationContext,
            PLAYBACK_CHANNEL_ID,
            R.string.playback_channel_name,
            R.string.playback_channel_description,
            PLAYBACK_NOTIFICATION_ID,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): CharSequence {
                    return video?.title ?: ""
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? =
                    video?.let {
                        PendingIntent.getActivity(
                            applicationContext,
                            0,
                            playerIntent(it),
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    }


                override fun getCurrentContentText(player: Player): CharSequence? {
                    return null
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    loadBitmap(video?.art, callback)
                    return null
                }
            },
            object : PlayerNotificationManager.NotificationListener {

                override fun onNotificationCancelled(notificationId: Int) {
                    // this will not immediately stop the service if the binding activity is still there.
                    // After calling this, the next time the activity is finished, the service will stop.

                    stopSelf()
                }

                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    if (ongoing) {
                        startForeground(notificationId, notification)
                    } else {
                        // if audio is paused, then stopForeground. it can be killed by dismissing notification
                        stopForeground(false)
                    }
                }
            }
        ).apply {
            setUseNextAction(false)
            setUsePreviousAction(false)
            setUseStopAction(false)

            setPlayer(player)
        }


        mediaSession = MediaSessionCompat(this, MEDIA_SESSION_TAG).apply {
            isActive = true
        }

        playerNotificationManager?.setMediaSessionToken(mediaSession.sessionToken)

        mediaSessionConnector = MediaSessionConnector(mediaSession).apply {
            setPlayer(player)
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        playerNotificationManager.setPlayer(null)
        player.release()
        mediaSession.release()

        super.onDestroy()
    }


    private fun loadBitmap(art: String?, callback: PlayerNotificationManager.BitmapCallback?) {
        if (art == null) {
            callback?.onBitmap(BitmapFactory.decodeResource(resources, R.drawable.plex_logo))
        } else {
            Picasso.get().load(PlexService.getMediaUrl(art))
                .into(object : com.squareup.picasso.Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        callback?.onBitmap(
                            bitmap ?: BitmapFactory.decodeResource(
                                resources,
                                R.drawable.plex_logo
                            )
                        )
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    }
                })

        }

    }


    inner class PlayerServiceBinder : Binder() {
        val service: PlayerService
            get() = this@PlayerService
        val player: ExoPlayer
            get() = this@PlayerService.player
    }

    fun addListener(callback: OnPlayerServiceCallback) {
        this.callback = callback
    }

    fun removeListener() {
        callback = null
    }


    fun play() {
        player.playWhenReady = true
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun pause() {
        player.playWhenReady = false
    }


    override fun onIsPlayingChanged(isPlaying: Boolean) {
        playing = isPlaying
        if (isPlaying) {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    if (playing) {
                        callback?.setPosition(player.currentPosition)
                        handler.postDelayed(this, 100)
                    }
                }
            }, 100)
        }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        callback?.setRepeatMode(repeatMode)
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        callback?.setIsPlaying(playWhenReady)
    }


    override fun onPlaybackStateChanged(state: Int) {

        callback?.setPlayerState(state)

        when (state) {
            Player.STATE_READY -> {
                if (!durationSet) {
                    durationSet = true
                    callback?.setDuration(player.duration)
                }
            }

            Player.STATE_ENDED -> {
                player.seekTo(0)
                player.playWhenReady = false
                callback?.setPosition(0)
            }

            else -> {

            }

        }
    }


    fun loadVideo(video: Video?) {
        if (this.video?.key != video?.key) {
            this.video = video
            val part = video?.media?.get(0)?.parts?.get(0)
            if (part != null) {
                val mediaItem = MediaItem.fromUri(PlexService.getMediaUrl(part.key))
                player.addMediaItem(mediaItem)
            }
            player.prepare()
            player.playWhenReady = true
            player.repeatMode = Player.REPEAT_MODE_OFF
        } else {
            callback?.setDuration(player.duration)
            callback?.setPlayerState(player.playbackState)
            callback?.setIsPlaying(player.playWhenReady)
            callback?.setRepeatMode(player.repeatMode)
        }

    }

    fun setRepeatMode(repeatMode: Int) {
        player.repeatMode = repeatMode
    }

}