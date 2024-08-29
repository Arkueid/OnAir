package com.arkueid.onair.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arkueid.onair.databinding.ActivityMoreBinding

class MoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.text = intent.getStringExtra("moreUrl") ?: "null"
    }
}