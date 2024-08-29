package com.arkueid.onair.ui.play

import android.media.MediaDataSource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSchemeDataSource
import androidx.media3.datasource.DataSourceUtil
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import com.arkueid.onair.databinding.FragmentPlayerBinding

/**
 * @author: Arkueid
 * @date: 2024/8/29
 * @desc:
 */
class PlayerFragment : Fragment() {

    companion object {
        const val TAG = "PlayerFragment"
    }

    private lateinit var binding: FragmentPlayerBinding
    private lateinit var player: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }


    private val url = "\n" +
            "http://192.168.137.1:8096/Videos/383107eba1d73105e0a144a070fd7c62/stream.mp4?Static=true&mediaSourceId=383107eba1d73105e0a144a070fd7c62&deviceId=TW96aWxsYS81LjAgKFdpbmRvd3MgTlQgMTAuMDsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzEyNy4wLjAuMCBTYWZhcmkvNTM3LjM2fDE3MjI2MDQwNTIwNjk1&api_key=3c50f169c177480f8e09847d490c27e8&Tag=a3bc2bff26103d4fd623c4000ddaad7e"

    @OptIn(UnstableApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        val mediaItem = MediaItem.fromUri(url)

        player.setMediaItem(mediaItem)
        player.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}