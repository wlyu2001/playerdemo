package com.shishiapp.playerdemo.di

import android.content.Context
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
class PlayerModule {

    @ServiceScoped
    @Provides
    fun providePlayer(@ApplicationContext context: Context): ExoPlayer {
        val bandwidthMeter = DefaultBandwidthMeter.Builder(context).build()
        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(
                    buildUponParameters()
                    .setMaxVideoSize(100, 100)
                        .setMaxVideoFrameRate(5)
            )
        }

        val loadControl = DefaultLoadControl()

        return SimpleExoPlayer.Builder(context).setBandwidthMeter(bandwidthMeter).setTrackSelector(trackSelector).setLoadControl(loadControl).build()
    }
}