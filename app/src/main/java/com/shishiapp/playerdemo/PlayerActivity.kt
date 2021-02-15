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
            playPause()
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

            viewModel.currentVideo.observe(this, { video ->
                playerService?.loadVideo(video)
            })
        }

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


    private fun playPause() {
        val viewModel = viewDataBinding.viewmodel

        if (viewModel != null) {
            if (viewModel.isPlayingData.value == true) {
                playerService?.pause()
            } else {
                playerService?.play()
            }
        }
    }


    fun stop() {

    }


}