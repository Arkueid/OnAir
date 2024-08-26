package com.arkueid.onair.ui.weekly

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkueid.onair.data.repository.WeeklyRepository
import com.arkueid.onair.event.weekly.WeeklySubjectEvent
import com.arkueid.onair.ui.weekly.model.WeeklyDataHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class WeeklyViewModel @Inject constructor(private val weeklyRepository: WeeklyRepository) :
    ViewModel() {

    companion object {
        private const val TAG = "WeeklyViewModel"
    }

    // is there any task running
    private var loading = false

    private val _loadingSuccess = MutableLiveData(false)
    val loadingState: LiveData<Boolean> = _loadingSuccess

    fun getWeeklySubjects() {
        // keep single task running
        if (loading) return

        loading = true
        var success = true
        viewModelScope.launch {
            weeklyRepository.getWeekly()
                .catch {
                    success = false
                    Log.e(TAG, "getWeeklySubjects: ")
                    it.printStackTrace()
                }
                .collect { data ->
                    EventBus.getDefault().removeStickyEvent(WeeklySubjectEvent::class.java)
                    EventBus.getDefault().postSticky(WeeklySubjectEvent(data))
                    _loadingSuccess.value = success
                }
            loading = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(null)
    }
}