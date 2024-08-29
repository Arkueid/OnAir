package com.arkueid.onair.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkueid.onair.data.repository.Repository
import com.arkueid.onair.domain.entity.Module
import com.arkueid.onair.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author: Arkueid
 * @date: 2024/8/27
 * @desc:
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val modules = repository.getHome()
        .transform {
            emit(Result.success(it))
        }
        .catch {
            throw it
//            emit(Result.failure(it, emptyList()))
        }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(null)
    }
}