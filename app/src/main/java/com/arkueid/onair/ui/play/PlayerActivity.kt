package com.arkueid.onair.ui.play

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import com.arkueid.onair.databinding.ActivityPlayerBinding


class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    private val url =
        "http://192.168.137.1:8096/Videos/383107eba1d73105e0a144a070fd7c62/stream.mp4?Static=true&mediaSourceId=383107eba1d73105e0a144a070fd7c62&deviceId=TW96aWxsYS81LjAgKFdpbmRvd3MgTlQgMTAuMDsgV2luNjQ7IHg2NCkgQXBwbGVXZWJLaXQvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lLzEyNy4wLjAuMCBTYWZhcmkvNTM3LjM2fDE3MjI2MDQwNTIwNjk1&api_key=3c50f169c177480f8e09847d490c27e8&Tag=a3bc2bff26103d4fd623c4000ddaad7e"

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}