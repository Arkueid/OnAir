package com.arkueid.onair.ui.home.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkueid.onair.data.repository.Repository
import com.arkueid.onair.entity.SearchResultData
import com.arkueid.onair.entity.SearchHistoryTag
import com.arkueid.onair.entity.SearchTipData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository,
    private val gson: Gson,
    private val mmkv: MMKV,
) : ViewModel() {
    companion object {
        private const val SEARCH_HISTORY = "search_history"
    }

    private val _searchHistory = MutableLiveData<MutableList<SearchHistoryTag>>()
    val searchHistory: LiveData<MutableList<SearchHistoryTag>> = _searchHistory

    private val _query = MutableStateFlow("")
    private val _searchTips = MutableStateFlow<SearchTipData>(emptyList())
    val searchTips: StateFlow<SearchTipData> = _searchTips

    private val _searchResults = MutableStateFlow<SearchResultData>(emptyList())
    val searchResults: StateFlow<SearchResultData> = _searchResults

    init {
        loadSearchHistory()
        viewModelScope.launch {
            _query.debounce(500) // limit the interval of triggering request
                .filter { it.isNotEmpty() } // no search for empty string
                .distinctUntilChanged() // ignore the same query
                .flatMapLatest { // query string -> search tips
                    repository.getSearchTip(it)
                }.collect { data ->
                    _searchTips.value = data
                }
        }
    }

    fun onQueryChanged(query: String) {
        _query.value = query
    }

    fun onQueryConfirmed(query: String?) {
        if (query.isNullOrEmpty()) return
        addSearchHistory(query)
        viewModelScope.launch {
            repository.getSearchResult(query).collectLatest {
                _searchResults.value = it
            }
        }
    }

    private fun loadSearchHistory() {
        val json = mmkv.decodeString(SEARCH_HISTORY)

        _searchHistory.postValue(
            if (json.isNullOrEmpty()) {
                mutableListOf()
            } else {
                gson.fromJson<MutableList<SearchHistoryTag>?>(
                    json,
                    object : TypeToken<List<SearchHistoryTag>>() {}.type
                ).apply {
                    sortBy { it.timestamp }
                }
            }
        )
    }

    private fun addSearchHistory(keyword: String) {
        val list = _searchHistory.value!!
        if (list.find { it.content == keyword } != null) return // already exists
        list.add(0, SearchHistoryTag(keyword, System.currentTimeMillis()))
        _searchHistory.postValue(list)
        mmkv.encode(SEARCH_HISTORY, gson.toJson(list))
    }

    fun clearSearchHistory() {
        _searchHistory.value = mutableListOf()
        mmkv.removeValueForKey(SEARCH_HISTORY)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(null)
    }
}