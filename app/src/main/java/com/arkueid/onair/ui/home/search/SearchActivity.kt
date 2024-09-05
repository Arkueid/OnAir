package com.arkueid.onair.ui.home.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkueid.onair.R
import com.arkueid.onair.common.view.TagText
import com.arkueid.onair.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), OnClickListener, TextWatcher {

    companion object {
        private const val TAG = "SearchActivity"
    }

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var tipsAdapter: ArrayAdapter<String>
    private lateinit var resultsAdapter: SearchResultAdapter

    private var confirmed = false

    @SuppressLint("NotifyDataSetChanged")
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
                        tagText.text = searchWords[i].content
                        // click on history tag
                        tagText.setOnClickListener(this)
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
        binding.searchInput.addTextChangedListener(this)
        binding.searchInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                confirmQuery(v.text.toString())
                true
            } else {
                false
            }
        }
        // tip list
        tipsAdapter = ArrayAdapter(this, R.layout.item_search_suggestion, R.id.content)
        binding.searchSuggestion.adapter = tipsAdapter
        // click on search tip
        binding.searchSuggestion.setOnItemClickListener { _, _, position, _ ->
            confirmQuery(tipsAdapter.getItem(position) ?: "")
        }

        // result list
        resultsAdapter = SearchResultAdapter()
        binding.searchResult.adapter = resultsAdapter
        binding.searchResult.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchTips.collect {
                    tipsAdapter.clear()
                    tipsAdapter.addAll(it)
                    tipsAdapter.notifyDataSetChanged()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResults.collect {
                    resultsAdapter.submitList(it)
                    if (it.isNotEmpty()) {
                        binding.searchSuggestion.visibility = View.GONE
                        binding.historyGroup.visibility = View.GONE
                        binding.searchResult.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun confirmQuery(query: String) {
        confirmed = true
        binding.searchInput.run {
            setText(query)
            setSelection(query.length)
        }
    }

    override fun onClick(v: View?) {
        if (v is TagText) {
            confirmQuery(v.text.toString())
            return
        }

        when (v!!.id) {
            R.id.backButton -> {
                onBackPressedDispatcher.onBackPressed()
            }

            R.id.searchButton -> {
                binding.searchInput.text.toString().let {
                    if (it.isNotEmpty()) {
                        // fetch search results and show
                        viewModel.onQueryConfirmed(it)
                    }
                }
            }

            R.id.clearHistory -> viewModel.clearSearchHistory()
        }
    }

    override fun finish() {
        super.finish()
        @Suppress("DEPRECATION")
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_bottom)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    @SuppressLint("NotifyDataSetChanged")
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        tipsAdapter.clear()
        tipsAdapter.notifyDataSetChanged()
        binding.searchResult.visibility = View.GONE
        if (s.isNullOrEmpty()) { // show history if input is empty
            binding.historyGroup.visibility = View.VISIBLE
            binding.searchSuggestion.visibility = View.GONE
        } else if (confirmed) { // straight query, triggered by clicking on tip item or history tag
            confirmed = false
            viewModel.onQueryConfirmed(s.toString())
        } else { // show tips
            binding.historyGroup.visibility = View.GONE
            binding.searchSuggestion.visibility = View.VISIBLE
            viewModel.onQueryChanged(s.toString())
        }
    }

    override fun afterTextChanged(s: Editable?) {}
}