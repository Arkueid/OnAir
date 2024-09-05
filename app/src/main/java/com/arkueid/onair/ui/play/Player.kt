package com.arkueid.onair.ui.play

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.exoplayer.ExoPlayer

/**
 * @author: Arkueid
 * @date: 2024/9/5
 * @desc: 根据生命周期控制播放器播放
 */

val ExoPlayer.lifecycleObserver
    get() = object : LifecycleEventObserver {
        private var lastIsPlaying = false
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (lastIsPlaying) play()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    lastIsPlaying = isPlaying
                    pause()
                }

                Lifecycle.Event.ON_DESTROY -> {
                    release()
                }

                else -> {}
            }
        }
    }