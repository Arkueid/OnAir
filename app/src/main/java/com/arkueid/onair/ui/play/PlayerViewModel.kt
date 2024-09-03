package com.arkueid.onair.ui.play

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tencent.mmkv.MMKV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import javax.inject.Inject

/**
 * @author: Arkueid
 * @date: 2024/8/31
 * @desc:
 */

@HiltViewModel
class PlayerViewModel @Inject constructor(private val mmkv: MMKV) : ViewModel() {

    companion object {
        const val KEY_SHOW_DANMAKU = "showDanmaku"
        const val KEY_VIDEO_SPEED = "videoSpeed"
        const val KEY_DANMAKU_SPEED = "danmakuSpeed"
        const val KEY_FILTER_STYLES = "filterStyles"
        const val KEY_DANMAKU_OPACITY = "danmakuOpacity"
        const val KEY_DANMAKU_SIZE = "danmakuSize"
        const val KEY_DANMAKU_VISIBLE_RANGE = "danmakuVisibleRange"
    }

    private val _videoSpeed = MutableLiveData<Float>()
    val videoSpeed: LiveData<Float> = _videoSpeed

    fun setVideoSpeed(speed: Float) {
        _videoSpeed.value = speed
        mmkv.encode(KEY_VIDEO_SPEED, speed)
    }

    private val _danmakuSpeed = MutableLiveData<Float>()
    val danmakuSpeed: LiveData<Float> = _danmakuSpeed
    fun setDanmakuSpeed(speed: Float) {
        _danmakuSpeed.value = speed
        mmkv.encode(KEY_DANMAKU_SPEED, speed)
    }

    private val _filterStyles = MutableLiveData<Int>()
    val filteredStyles: LiveData<Int> = _filterStyles
    fun setFilterStyles(styles: Int) {
        _filterStyles.value = styles
        mmkv.encode(KEY_FILTER_STYLES, styles)
    }

    private val _danmakuOpacity = MutableLiveData<Int>()
    val danmakuAlpha: LiveData<Int> = _danmakuOpacity
    fun setDanmakuOpacity(opacity: Int) {
        _danmakuOpacity.value = opacity
        mmkv.encode(KEY_DANMAKU_OPACITY, opacity)
    }

    private val _danmakuSize = MutableLiveData<Float>()
    val danmakuSize: LiveData<Float> = _danmakuSize
    fun setDanmakuSize(size: Float) {
        _danmakuSize.value = size
        mmkv.encode(KEY_DANMAKU_SIZE, size)
    }

    private val _danmakuVisibleRange = MutableLiveData<Int>()
    val danmakuVisibleRange: LiveData<Int> = _danmakuVisibleRange
    fun setDanmakuVisibleRange(range: Int) {
        _danmakuVisibleRange.value = range
        mmkv.encode(KEY_DANMAKU_VISIBLE_RANGE, range)
    }

    private var _showDanmaku = true
    var showDanmaku
        get() = _showDanmaku
        set(value) {
            _showDanmaku = value
            mmkv.encode(KEY_SHOW_DANMAKU, value)
        }

    init {
        _showDanmaku = mmkv.decodeBool(KEY_SHOW_DANMAKU, true)
        _videoSpeed.value = mmkv.decodeFloat(KEY_VIDEO_SPEED, 1f)
        _danmakuSpeed.value = mmkv.decodeFloat(KEY_DANMAKU_SPEED, 1f)
        _filterStyles.value = mmkv.decodeInt(KEY_FILTER_STYLES, 0)
        _danmakuOpacity.value = mmkv.decodeInt(KEY_DANMAKU_OPACITY, 255)
        _danmakuSize.value = mmkv.decodeFloat(KEY_DANMAKU_SIZE, 16f)
        _danmakuVisibleRange.value = mmkv.decodeInt(KEY_DANMAKU_VISIBLE_RANGE, 4)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(null)
    }
}