package com.arkueid.onair.ui.weekly

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkueid.onair.data.repository.WeeklyRepository
import com.arkueid.onair.ui.weekly.model.WeeklyDataHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyViewModel @Inject constructor(private val weeklyRepository: WeeklyRepository) :
    ViewModel() {

    companion object {
        private const val TAG = "WeeklyViewModel"
    }

    private val _weeklySubjects = MutableLiveData(WeeklyDataHolder(emptyList()))

    val weeklySubjects: LiveData<WeeklyDataHolder>
        get() = _weeklySubjects

    private val _loadingSuccess = MutableLiveData(false)
    val loadingState: LiveData<Boolean> = _loadingSuccess

    fun getWeeklySubjects() {
        var success = true
        viewModelScope.launch {
            weeklyRepository.getWeekly()
                .catch {
                    success = false
                    emit(_weeklySubjects.value!!)
                    Log.e(TAG, "getWeeklySubjects: ")
                    it.printStackTrace()
                }
                .collect { data ->
                    _loadingSuccess.value = success
                    _weeklySubjects.postValue(data)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(null)
    }
}