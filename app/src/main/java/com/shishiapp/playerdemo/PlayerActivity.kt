package com.shishiapp.playerdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.Player
import com.shishiapp.playerdemo.databinding.ActivityPlayerBinding
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.service.PlayerService
import com.shishiapp.playerdemo.viewmodel.PlayerViewModel
import java.util.*


private const val INTENT_VIDEO_KEY = "video_key"

fun Context.playerIntent(video: Video): Intent {
    return Intent(this, PlayerActivity::class.java).apply {
        putExtra(INTENT_VIDEO_KEY, video.key)
    }
}

fun Long.toDurationString(): String {
    val secs: Long = this / 1000
    return String.format("%02d:%02d:%02d", secs / 3600, secs % 3600 / 60, secs % 60)
}

class PlayerActivity : AppCompatActivity() {


    private lateinit var viewDataBinding: ActivityPlayerBinding
    private var playerService: PlayerService? = null
    private var bound = false
    private var duration = -1L


    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            if (binder is PlayerService.PlayerServiceBinder) {

                viewDataBinding.playerView.player = binder.player
                playerService = binder.service


                viewDataBinding.viewmodel?.let {
                    playerService?.addListener(it)

                    it.setCurrentVideo(intent.extras?.getString(INTENT_VIDEO_KEY))
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityPlayerBinding.inflate(layoutInflater).apply {

            viewmodel =
                ViewModelProvider(this@PlayerActivity).get(PlayerViewModel::class.java)
            lifecycleOwner = this@PlayerActivity
        }

        setContentView(viewDataBinding.root)

        viewDataBinding.buttonPlayPause.setOnClickListener {
            viewDataBinding.viewmodel?.let { viewModel ->
                if (viewModel.isPlayingData.value == true) {
                    playerService?.pause()
                } else {
                    // Here, we need to startService again in case the user pauses playback and dismiss notification (stopSelf) which turns a started and bound service into a merely bound service.
                    startService(Intent(this, PlayerService::class.java))
                    playerService?.play()
                }
            }
        }
        viewDataBinding.seekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    playerService?.seekTo(progress * duration / 100)
                }
            }

            override fun onStartTrackingTouch(seek: SeekBar?) {
            }

            override fun onStopTrackingTouch(seek: SeekBar?) {
            }
        })

        viewDataBinding.buttonRepeat.setOnClickListener {
            viewDataBinding.viewmodel?.let { viewModel ->

                when (viewModel.repeatModeData.value) {
                    Player.REPEAT_MODE_OFF -> playerService?.setRepeatMode(Player.REPEAT_MODE_ALL)
                    Player.REPEAT_MODE_ALL -> playerService?.setRepeatMode(Player.REPEAT_MODE_ONE)
                    Player.REPEAT_MODE_ONE -> playerService?.setRepeatMode(Player.REPEAT_MODE_OFF)
                }
            }
        }


        viewDataBinding.viewmodel?.let { viewModel ->

            viewModel.durationData.observe(this, {
                duration = it
                viewDataBinding.duration.text = it.toDurationString()
            })

            viewModel.positionData.observe(this, {
                if (duration > 0) {
                    viewDataBinding.seekbar.progress = (it * 100L / duration).toInt()
                }
                viewDataBinding.position.text = it.toDurationString()
            })

            viewModel.isPlayingData.observe(this, {
                viewDataBinding.buttonPlayPause.text =
                    if (it) getString(R.string.pause) else getString(R.string.play)
            })

            viewModel.playerStateData.observe(this, {
                viewDataBinding.textviewState.text =

                    when (it) {
                        Player.STATE_BUFFERING -> {
                            getString(R.string.buffering)
                        }

                        Player.STATE_READY -> {
                            getString(R.string.ready)
                        }

                        Player.STATE_ENDED -> {
                            getString(R.string.ended)
                        }

                        Player.STATE_IDLE -> {
                            getString(R.string.idle)
                        }
                        else -> ""
                    }

                if (it == Player.STATE_ENDED) {
                    playerService?.seekTo(0)
                    playerService?.pause()

                }
            })

            viewModel.repeatModeData.observe(this, {
                viewDataBinding.buttonRepeat.text =
                    when (it) {
                        Player.REPEAT_MODE_OFF -> {
                            getString(R.string.repeat_off)
                        }

                        Player.REPEAT_MODE_ONE -> {
                            getString(R.string.repeat_one)
                        }

                        Player.REPEAT_MODE_ALL -> {
                            getString(R.string.repeat_all)
                        }

                        else -> ""
                    }
            })


            viewModel.currentVideoData.observe(this, { video ->
                playerService?.loadVideo(video)
            })

            viewModel.setRepeatMode(Player.REPEAT_MODE_OFF)
        }

        // startService is needed if we want to play in the background
        // no need to call startForegroundService, because PlayerNotificationManager will call startForeground inside the Service

        startService(Intent(this, PlayerService::class.java))

        bindPlayerService()
    }

    private fun bindPlayerService() {
        if (!bound) {
            bindService(
                Intent(this, PlayerService::class.java),
                connection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

}