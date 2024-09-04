package com.arkueid.onair.ui.play

import android.content.res.Configuration
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.arkueid.onair.R
import com.arkueid.onair.databinding.PopupPlayerSettingsBinding
import com.arkueid.onair.entity.DanmakuItem
import java.util.Locale
import kotlin.math.round

/**
 * @author: Arkueid
 * @date: 2024/9/2
 * @desc:
 */
class PlayerSettingsPopup(
    inflater: LayoutInflater,
    private val listener: OnPlayerSettingsChangedListener?,
    private val binding: PopupPlayerSettingsBinding = PopupPlayerSettingsBinding.inflate(
        inflater
    )
) : PopupWindow(
    binding.root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true
) {

    private var _filterStyles: Int = 0

    interface OnPlayerSettingsChangedListener {
        fun onDanmakuFontSizeChanged(size: Float)
        fun onDanmakuVisibleRangeChanged(range: Int)
        fun onDanmakuOpacityChanged(opacity: Int)
        fun onDanmakuSpeedChanged(speed: Float)
        fun onDanmakuFilterStylesChanged(styles: Int)
        fun onVideoSpeedChanged(speed: Float)
    }

    var filteredStyles: Int
        get() = _filterStyles
        set(value) {
            _filterStyles = value
            binding.danmakuFilterTopCheckBox.isChecked = value and DanmakuItem.Style.TOP != 0
            binding.danmakuFilterBottomCheckBox.isChecked = value and DanmakuItem.Style.BOTTOM != 0
            binding.danmakuFilterRollingCheckBox.isChecked =
                value and DanmakuItem.Style.ROLLING != 0
            listener?.onDanmakuFilterStylesChanged(_filterStyles)
        }

    var danmakuSize: Float
        get() = binding.danmakuFontSizeSlider.value
        set(value) {
            binding.danmakuFontSizeSlider.value = when (value) {
                14f -> 1f
                16f -> 2f
                18f -> 3f
                else -> 2f
            }
        }

    var danmakuVisibleRange: Int
        get() = binding.danmakuVisibleRangeSlider.value.toInt()
        set(value) {
            binding.danmakuVisibleRangeSlider.value = value.toFloat()
        }

    var danmakuSpeed: Float
        get() = binding.danmakuSpeedSlider.value
        set(value) {
            binding.danmakuSpeedSlider.value = value
        }

    var danmakuAlpha: Int
        get() = binding.danmakuTransparencySlider.value.toInt()
        set(value) {
            binding.danmakuTransparencySlider.value = value.toFloat()
        }

    var videoSpeed: Float
        get() = binding.videoPlaybackSpeedSlider.value
        set(value) {
            binding.videoPlaybackSpeedSlider.value = value
        }

    init {
        contentView = binding.root

        binding.danmakuFilterTopCheckBox.setOnCheckedChangeListener { _, isChecked ->
            _filterStyles = if (isChecked) {
                _filterStyles or DanmakuItem.Style.TOP
            } else {
                _filterStyles and (DanmakuItem.Style.TOP.inv() and 0b111)
            }
            listener?.onDanmakuFilterStylesChanged(_filterStyles)
        }

        binding.danmakuFilterBottomCheckBox.setOnCheckedChangeListener { _, isChecked ->
            _filterStyles = if (isChecked) {
                _filterStyles or DanmakuItem.Style.BOTTOM
            } else {
                _filterStyles and (DanmakuItem.Style.BOTTOM.inv() and 0b111)
            }
            listener?.onDanmakuFilterStylesChanged(_filterStyles)
        }

        binding.danmakuFilterRollingCheckBox.setOnCheckedChangeListener { _, isChecked ->
            _filterStyles = if (isChecked) {
                _filterStyles or DanmakuItem.Style.ROLLING
            } else {
                _filterStyles and (DanmakuItem.Style.ROLLING.inv() and 0b111)
            }
            listener?.onDanmakuFilterStylesChanged(_filterStyles)
        }

        binding.danmakuFontSizeSlider.addOnChangeListener { _, value, fromUser ->
            binding.danmakuFontSizeText.text = when (value) {
                1f -> "小"
                2f -> "中"
                3f -> "大"
                else -> "未知"
            }
            if (fromUser) {
                listener?.onDanmakuFontSizeChanged(
                    when (value) {
                        1f -> 14f
                        2f -> 16f
                        3f -> 18f
                        else -> 16f
                    }
                )
            }
        }

        binding.danmakuVisibleRangeSlider.addOnChangeListener { _, value, fromUser ->
            binding.danmakuVisibleRangeText.text = when (value) {
                1f -> "1/4"
                2f -> "1/2"
                3f -> "3/4"
                4f -> "全部"
                else -> "未知"
            }
            if (fromUser) {
                listener?.onDanmakuVisibleRangeChanged(value.toInt())
            }
        }

        binding.danmakuSpeedSlider.addOnChangeListener { _, value, fromUser ->
            binding.danmakuSpeedText.text = String.format(Locale.getDefault(), "%.1f", value)
            if (fromUser) {
                listener?.onDanmakuSpeedChanged(value)
            }
        }

        binding.danmakuTransparencySlider.addOnChangeListener { _, value, fromUser ->
            binding.danmakuTransparencyText.text =
                String.format(Locale.getDefault(), "%.1f", value / 255f)
            if (fromUser) {
                listener?.onDanmakuOpacityChanged(value.toInt())
            }
        }

        binding.videoPlaybackSpeedSlider.addOnChangeListener { _, value, fromUser ->
            binding.videoPlaybackSpeedText.text =
                String.format(Locale.getDefault(), "%.2f", value)
            if (fromUser) {
                listener?.onVideoSpeedChanged(value)
            }
        }

    }

    fun show(parent: View) {
        val gravity: Int
        if (parent.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            animationStyle = R.style.Theme_OnAir_PopupRight
            gravity = Gravity.END
            width = if (parent.resources.configuration.screenWidthDp >= 400) {
                round(parent.resources.displayMetrics.widthPixels * 0.5f).toInt()
            } else {
                parent.resources.displayMetrics.widthPixels
            }
        } else {
            animationStyle = R.style.Theme_OnAir_PopupBottom
            gravity = Gravity.BOTTOM
            width = parent.resources.displayMetrics.widthPixels
        }
        super.showAtLocation(
            parent, gravity, 0, 0
        )
    }
}