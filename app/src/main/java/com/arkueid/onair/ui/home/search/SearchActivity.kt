package com.arkueid.onair.ui.home.search

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.arkueid.onair.R
import com.arkueid.onair.common.TagText
import com.arkueid.onair.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.searchHistory.observe(this) { searchWords ->
            binding.tagLayout.let {
                val numToAdd = searchWords.size - it.childCount
                if (numToAdd > 0) {
                    for (i in 0 until numToAdd) {
                        val tagText = TagText(this)
                        tagText.text = searchWords[i]
                        it.addView(tagText)
                    }
                } else {
                    it.removeAllViews()
                }
            }
        }

        binding.backButton.setOnClickListener(this)

        binding.searchButton.setOnClickListener(this)

        binding.clearHistory.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.backButton -> {
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.searchButton -> {
                binding.searchInput.text.toString().let {
                    if (it.isNotEmpty())
                        viewModel.addSearchHistory(it)
                }
            }

            R.id.clearHistory -> viewModel.clearSearchHistory()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_bottom)
    }
}