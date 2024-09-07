package com.arkueid.onair.ui.play

import android.content.pm.ActivityInfo
import android.content.res.Configuration
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.arkueid.onair.R
import com.arkueid.onair.common.timeString
import com.arkueid.onair.databinding.FragmentPlayerBinding
import com.arkueid.onair.domain.toDisplay
import com.arkueid.plugin.data.entity.Anime
import com.arkueid.onair.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author: Arkueid
 * @date: 2024/8/29
 * @desc:
 */
@AndroidEntryPoint
class PlayerFragment : Fragment(), SurfaceHolder.Callback, OnClickListener, Player.Listener,
    OnSeekBarChangeListener, PlayerSettingsPopup.OnPlayerSettingsChangedListener {

    companion object {
        const val TAG = "PlayerFragment"
        private const val FPS = 30L
        private const val UPDATE_INTERVAL = 1000L / FPS
    }

    private lateinit var binding: FragmentPlayerBinding
    private lateinit var player: ExoPlayer
    private val viewModel: PlayerViewModel by viewModels()
    private lateinit var popupWindow: PlayerSettingsPopup

    private lateinit var anime: Anime

    // temp var
    private var isEnd = false
    private var isLocked = false
    private var isControlShowing = false
    private var resolution: Pair<Int, Int> = 16 to 9

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
        player.addListener(this)
        lifecycle.addObserver(player.lifecycleObserver)

        binding.surfaceView.holder.addCallback(this)
        binding.playerControl.root.setOnClickListener(this)
        binding.playerControl.playBtn.setOnClickListener(this)
        binding.playerControl.lockBtn.setOnClickListener(this)
        binding.playerControl.fullscreenBtn.setOnClickListener(this)
        binding.playerControl.danmakuBtn.setOnClickListener(this)
        binding.playerControl.optionBtn.setOnClickListener(this)
        binding.playerControl.exitBtn.setOnClickListener(this)
        binding.playerControl.seekBar.setOnSeekBarChangeListener(this)

        // 会被自动释放
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )

        observe()

        view.post { showControl() }

        arguments?.run {
            anime = requireArguments().getParcelable("anime")!!
            play(anime.url)

            lifecycleScope.launch {
                viewModel.getDanmakus(anime).collect { result ->
                    if (result.isSuccess) {
                        binding.danmakuView.danmakus = result.data!!.map { it.toDisplay() }
                        Log.d(TAG, "postUpdateProgress: ${binding.danmakuView.danmakus}")
                    } else {
                        ToastUtils.showToast("Failed to fetch danmakus")
                    }
                }
            }
        }
    }

    fun play(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    private fun observe() {
        popupWindow = PlayerSettingsPopup(layoutInflater, this)

        binding.danmakuView.visibility = if (viewModel.showDanmaku) View.VISIBLE else View.GONE
        binding.playerControl.danmakuBtn.isSelected = !viewModel.showDanmaku

        popupWindow.filteredStyles = viewModel.filteredStyles.value!!
        popupWindow.danmakuSize = viewModel.danmakuSize.value!!
        popupWindow.danmakuSpeed = viewModel.danmakuSpeed.value!!
        popupWindow.danmakuAlpha = viewModel.danmakuAlpha.value!!
        popupWindow.danmakuVisibleRange = viewModel.danmakuVisibleRange.value!!
        popupWindow.videoSpeed = viewModel.videoSpeed.value!!

        viewModel.danmakuSize.observe(viewLifecycleOwner) {
            binding.danmakuView.danmakuSize = it
        }

        viewModel.videoSpeed.observe(viewLifecycleOwner) {
            player.setPlaybackSpeed(it)
        }

        viewModel.danmakuSpeed.observe(viewLifecycleOwner) {
            binding.danmakuView.speedScale = it
        }

        viewModel.danmakuAlpha.observe(viewLifecycleOwner) {
            binding.danmakuView.danmakuAlpha = it
        }

        viewModel.filteredStyles.observe(viewLifecycleOwner) {
            binding.danmakuView.filteredStyles = it
        }

        viewModel.danmakuVisibleRange.observe(viewLifecycleOwner) {
            binding.danmakuView.danmakuTrackRange = it
        }

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
        binding.playerControl.currentPositionText.text = player.currentPosition.timeString
        binding.playerControl.durationText.text = player.duration.timeString
        binding.danmakuView.progress = player.currentPosition
    }

    private fun postUpdateProgress() {
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

    private fun toggleFullscreen() {
        binding.playerControl.fullscreenBtn.run {
            isSelected = !isSelected
            requireActivity().requestedOrientation = if (isSelected) {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }

    private fun toggleLock() {
        binding.playerControl.lockBtn.run {
            isLocked = !isLocked
            isSelected = isLocked
            if (isSelected) {
                binding.playerControl.buttonsNoLock.visibility = View.INVISIBLE
            } else {
                binding.playerControl.buttonsNoLock.visibility = View.VISIBLE
            }
        }
    }

    private fun toggleShowControl() {
        if (!isControlShowing) {
            showControl()
        } else {
            hideControl()
        }
    }

    private val hideRunnable = Runnable { hideControl() }

    private fun hideControl() {
        binding.playerControl.allButtons.visibility = View.GONE
        isControlShowing = false
    }

    private fun showControl() {
        isControlShowing = true
        val group = if (isLocked) {
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
        binding.playerControl.danmakuBtn.run {
            viewModel.showDanmaku = !viewModel.showDanmaku
            isSelected = !viewModel.showDanmaku
            binding.danmakuView.visibility = if (viewModel.showDanmaku) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun togglePlay() {
        // relay when stopped
        if (isEnd) {
            isEnd = false
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
            isEnd = true
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
                layoutParams = layoutParams.apply {
                    width = binding.space.height * resolution.first / resolution.second
                }
            }
        }

    }

    override fun onVideoSizeChanged(videoSize: VideoSize) {
        super.onVideoSizeChanged(videoSize)
        resolution = Pair(videoSize.width, videoSize.height)
    }


    override fun onDanmakuFontSizeChanged(size: Float) {
        viewModel.setDanmakuSize(size)
    }

    override fun onDanmakuVisibleRangeChanged(range: Int) {
        viewModel.setDanmakuVisibleRange(range)
    }

    override fun onDanmakuOpacityChanged(opacity: Int) {
        viewModel.setDanmakuOpacity(opacity)
    }

    override fun onDanmakuSpeedChanged(speed: Float) {
        viewModel.setDanmakuSpeed(speed)
    }

    override fun onDanmakuFilterStylesChanged(styles: Int) {
        viewModel.setFilterStyles(styles)
    }

    override fun onVideoSpeedChanged(speed: Float) {
        viewModel.setVideoSpeed(speed)
    }
}