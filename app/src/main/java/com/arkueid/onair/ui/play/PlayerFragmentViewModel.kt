package com.arkueid.onair.ui.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel

/**
 * @author: Arkueid
 * @date: 2024/8/31
 * @desc:
 */
class PlayerFragmentViewModel : ViewModel() {

    var lastIsPlaying = false

    var isLocked = false

    var isEnd = false

    var showControl = true

    var showDanmaku = true

    var isFullScreen = false

    var resolution = Pair(16, 9)

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(null)
    }
}