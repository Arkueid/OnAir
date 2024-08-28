package com.arkueid.onair.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkueid.onair.data.repository.Repository
import com.arkueid.onair.domain.entity.Module
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author: Arkueid
 * @date: 2024/8/27
 * @desc:
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var isLoading = false
    private val _loadingState = MutableLiveData(false)
    val loadingState: LiveData<Boolean> = _loadingState
    private val _modules = MutableLiveData<List<Module>>()
    val modules: LiveData<List<Module>> = _modules

    fun getModuleData() {
        if (isLoading) return
        isLoading = true

        var success = true
        viewModelScope.launch {
            repository.getHome()
                .catch {
                    success = false
                    emit(emptyList())
                }
                .collect {
                    _loadingState.value = success
                    _modules.postValue(it)
                }
            isLoading = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(null)
    }
}