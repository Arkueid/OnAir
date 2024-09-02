package com.arkueid.onair.ui.play

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.OnBackPressedCallback
import androidx.annotation.OptIn
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.arkueid.onair.R
import com.arkueid.onair.databinding.FragmentPlayerBinding
import com.arkueid.onair.ui.play.danmaku.DanmakuItem
import kotlin.random.Random

/**
 * @author: Arkueid
 * @date: 2024/8/29
 * @desc:
 */

class PlayerFragment : Fragment(), SurfaceHolder.Callback, OnClickListener, Player.Listener,
    OnSeekBarChangeListener, PlayerSettingsPopup.OnPlayerSettingsChangedListener {

    companion object {
        const val TAG = "PlayerFragment"
        private const val FPS = 30L
        private const val UPDATE_INTERVAL = 1000L / FPS
    }

    private lateinit var binding: FragmentPlayerBinding
    private lateinit var player: ExoPlayer
    private val viewModel: PlayerFragmentViewModel by viewModels()
    private lateinit var popupWindow: PlayerSettingsPopup

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                toggleFullscreen()
            } else {
                requireActivity().finish()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }


    @OptIn(UnstableApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        player = ExoPlayer.Builder(requireContext()).build()
        val mediaItem = MediaItem.fromUri(Uri.parse("rawresource:///${R.raw.test}"))

        // --for test--
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
        // --for test--
        player.addListener(this)

        binding.surfaceView.holder.addCallback(this)
        binding.playerControl.root.setOnClickListener(this)
        binding.playerControl.playBtn.setOnClickListener(this)
        binding.playerControl.lockBtn.setOnClickListener(this)
        binding.playerControl.fullscreenBtn.setOnClickListener(this)
        binding.playerControl.danmakuBtn.setOnClickListener(this)
        binding.playerControl.optionBtn.setOnClickListener(this)
        binding.playerControl.exitBtn.setOnClickListener(this)
        binding.playerControl.seekBar.setOnSeekBarChangeListener(this)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )

        observe()

        view.post { showControl() }
    }

    private fun observe() {
        popupWindow = PlayerSettingsPopup(layoutInflater)
        popupWindow.listener = this
    }

    override fun onPause() {
        super.onPause()
        viewModel.lastIsPlaying = player.isPlaying
        player.pause()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.lastIsPlaying) {
            player.play()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        onBackPressedCallback.remove()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        player.setVideoSurfaceHolder(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        player.clearVideoSurfaceHolder(holder)
    }

    override fun onClick(v: View?) {
        v ?: return

        when (v.id) {
            R.id.playBtn -> togglePlay()

            R.id.lockBtn -> toggleLock()

            R.id.playerControl -> toggleShowControl()

            R.id.danmakuBtn -> toggleDanmaku()

            R.id.fullscreenBtn -> toggleFullscreen()

            R.id.optionBtn -> showPlayerSettings()

            R.id.exitBtn -> requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showPlayerSettings() {
        popupWindow.show(binding.root)
    }

    /**
     * update view except seek bar
     */
    private fun updateProgress() {
        binding.playerControl.currentPositionText.text = parseTime(player.currentPosition)
        binding.playerControl.durationText.text = parseTime(player.duration)
        binding.danmakuView.progress = player.currentPosition
    }

    private val danmakus = {
        val list = mutableListOf<DanmakuItem>()
        val styles =
            listOf(DanmakuItem.Style.ROLLING, )
        for (i in 0..1200) {
            val p = Random.nextLong(0, player.duration)
            list.add(
                DanmakuItem(
                    progress = p, // 时间范围 0 到 7分27秒
                    content = "弹幕${parseTime(p)}.${p % 1000}",
                    color = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt()), // 随机颜色
                    style = styles.random()
                )
            )
        }
        list.apply { sortBy { it.progress } }
    }

    private fun postUpdateProgress() {
        // TODO fetch from viewModel
        binding.danmakuView.danmakus = danmakus()
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                binding.playerControl.seekBar.let {
                    it.max = player.duration.toInt()
                    it.progress = player.currentPosition.toInt()
                }

                updateProgress()

                if (player.isPlaying) {
                    handler.postDelayed(this, UPDATE_INTERVAL)
                }
            }
        }
        handler.post(runnable)
    }

    private fun parseTime(time: Long): String {
        val hour = time / 3600000
        val minute = time % 3600000 / 60000
        val second = time % 60000 / 1000
        return if (hour > 0) {
            "%02d:%02d:%02d".format(hour, minute, second)
        } else {
            "%02d:%02d".format(minute, second)
        }
    }

    private fun toggleFullscreen() {
        binding.playerControl.fullscreenBtn.let {
            viewModel.isFullScreen = !viewModel.isFullScreen
            it.isSelected = viewModel.isFullScreen
        }
        requireActivity().requestedOrientation = if (viewModel.isFullScreen) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun toggleLock() {
        binding.playerControl.lockBtn.let {
            viewModel.isLocked = !viewModel.isLocked
            it.isSelected = viewModel.isLocked
            if (it.isSelected) {
                binding.playerControl.buttonsNoLock.visibility = View.INVISIBLE
            } else {
                binding.playerControl.buttonsNoLock.visibility = View.VISIBLE
            }
        }
    }

    private fun toggleShowControl() {
        viewModel.showControl = !viewModel.showControl
        if (viewModel.showControl) {
            showControl()
        } else {
            hideControl()
        }
    }

    private val hideRunnable = Runnable { hideControl() }

    private fun hideControl() {
        binding.playerControl.allButtons.visibility = View.GONE
    }

    private fun showControl() {
        val group = if (viewModel.isLocked) {
            binding.playerControl.lockBtn
        } else {
            binding.playerControl.allButtons
        }
        group.visibility = View.VISIBLE
        // hide control after 5s
        group.removeCallbacks(hideRunnable)
        group.postDelayed(hideRunnable, 5000)
    }

    private fun toggleDanmaku() {
        binding.playerControl.danmakuBtn.let {
            viewModel.showDanmaku = !viewModel.showDanmaku
            it.isSelected = !viewModel.showDanmaku
            binding.danmakuView.visibility = if (viewModel.showDanmaku) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun togglePlay() {
        // relay when stopped
        if (viewModel.isEnd) {
            viewModel.isEnd = false
            player.seekTo(0)
        }

        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        // is initializing source or buffering after position change
        binding.playerControl.loadingProgress.visibility =
            if (playbackState == Player.STATE_BUFFERING) {
                View.VISIBLE
            } else {
                View.GONE
            }

        if (playbackState == Player.STATE_ENDED) {
            viewModel.isEnd = true
            binding.playerControl.playBtn.setImageResource(R.drawable.baseline_replay_24)
        } else {
            binding.playerControl.playBtn.setImageResource(R.drawable.button_play)
        }
    }

    override fun onIsLoadingChanged(isLoading: Boolean) {
        super.onIsLoadingChanged(isLoading)
        binding.playerControl.playBtn.isEnabled = !isLoading
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        if (isPlaying) {
            binding.playerControl.playBtn.isSelected = true
            postUpdateProgress()
        } else {
            binding.playerControl.playBtn.isSelected = false
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            player.seekTo(progress.toLong())
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        updateProgress()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.space.post {
            binding.surfaceView.run {
                layoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply {
                    width =
                        binding.space.height * viewModel.resolution.first / viewModel.resolution.second
                }
            }
        }

    }

    override fun onVideoSizeChanged(videoSize: VideoSize) {
        super.onVideoSizeChanged(videoSize)
        viewModel.resolution = Pair(videoSize.width, videoSize.height)
    }

    override fun onDanmakuFontSizeChanged(sizeFlag: Int) {
        binding.danmakuView.fontSize = when (sizeFlag) {
            1 -> 14f
            2 -> 16f
            3 -> 18f
            else -> 16f
        }
    }

    override fun onDanmakuVisibleRangeChanged(range: Int) {
        binding.danmakuView.trackRange = range
    }

    override fun onDanmakuOpacityChanged(opacity: Int) {
        binding.danmakuView.danmakuAlpha = opacity
    }

    override fun onDanmakuSpeedChanged(speed: Float) {
        binding.danmakuView.speedScale = speed
    }

    override fun onDanmakuFilterStylesChanged(styles: Int) {
        binding.danmakuView.filteredStyles = styles
    }

    override fun onVideoSpeedChanged(speed: Float) {
        player.setPlaybackSpeed(speed)
    }
}