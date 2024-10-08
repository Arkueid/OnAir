package com.arkueid.onair.ui.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkueid.onair.data.repository.Repository
import com.arkueid.plugin.data.entity.WeeklyAnime
import com.arkueid.onair.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeeklyViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    companion object {
        private const val TAG = "WeeklyViewModel"
    }

    val currentWeeklySubjects = MutableStateFlow<List<List<WeeklyAnime>>>(emptyList())

    val weeklySubjects = repository.getWeekly()
        .transform {
            emit(Result.success(it))
        }
        .catch {
            emit(Result.failure(it, emptyList()))
        }.onEach {
            currentWeeklySubjects.value = it.data ?: emptyList()
        }

    val currentTabIndex: Int
        get() {
            return LocalDate.now().dayOfWeek.value - 1
        }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(null)
    }
}