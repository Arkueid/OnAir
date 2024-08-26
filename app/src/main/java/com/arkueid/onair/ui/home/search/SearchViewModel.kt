package com.arkueid.onair.ui.home.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.cancel

class SearchViewModel : ViewModel() {
    companion object {
        private const val SEARCH_HISTORY = "search_history"
    }

    private val mmvk = MMKV.defaultMMKV()
    private val _searchHistory = MutableLiveData<MutableList<String>>()
    val searchHistory: LiveData<MutableList<String>> = _searchHistory

    init {
        loadSearchHistory()
    }

    private fun loadSearchHistory() {
        _searchHistory.postValue(
            mmvk.decodeStringSet(SEARCH_HISTORY, mutableSetOf())!!.toMutableList()
        )
    }

    fun addSearchHistory(keyword: String) {
        val list = _searchHistory.value!!
        list.add(0, keyword)
        _searchHistory.postValue(list)
        mmvk.encode(SEARCH_HISTORY, _searchHistory.value!!.toMutableSet())
    }

    fun clearSearchHistory() {
        _searchHistory.value = mutableListOf()
        mmvk.encode(SEARCH_HISTORY, _searchHistory.value!!.toMutableSet())
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(null)
    }
}