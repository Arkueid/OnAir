package com.arkueid.onair.ui.play

import android.content.res.Configuration
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.arkueid.onair.R
import com.arkueid.onair.databinding.PopupPlayerSettingsBinding
import com.arkueid.onair.ui.play.danmaku.DanmakuItem
import java.util.Locale
import kotlin.math.round

/**
 * @author: Arkueid
 * @date: 2024/9/2
 * @desc:
 */
class PlayerSettingsPopup(
    inflater: LayoutInflater,
    private val binding: PopupPlayerSettingsBinding = PopupPlayerSettingsBinding.inflate(
        inflater
    )
) : PopupWindow(
    binding.root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true
) {

    private var filterStyles: Int = 0

    interface OnPlayerSettingsChangedListener {
        fun onDanmakuFontSizeChanged(sizeFlag: Int)
        fun onDanmakuVisibleRangeChanged(range: Int)
        fun onDanmakuOpacityChanged(opacity: Int)
        fun onDanmakuSpeedChanged(speed: Float)
        fun onDanmakuFilterStylesChanged(styles: Int)
        fun onVideoSpeedChanged(speed: Float)
    }

    var listener: OnPlayerSettingsChangedListener? = null

    init {
        contentView = binding.root

        binding.danmakuFilterTopCheckBox.setOnCheckedChangeListener { _, isChecked ->
            filterStyles = if (isChecked) {
                filterStyles or DanmakuItem.Style.TOP
            } else {
                filterStyles and (DanmakuItem.Style.TOP.inv() and 0b111)
            }
            listener?.onDanmakuFilterStylesChanged(filterStyles)
        }

        binding.danmakuFilterBottomCheckBox.setOnCheckedChangeListener { _, isChecked ->
            filterStyles = if (isChecked) {
                filterStyles or DanmakuItem.Style.BOTTOM
            } else {
                filterStyles and (DanmakuItem.Style.BOTTOM.inv() and 0b111)
            }
            listener?.onDanmakuFilterStylesChanged(filterStyles)
        }

        binding.danmakuFilterRollingCheckBox.setOnCheckedChangeListener { _, isChecked ->
            filterStyles = if (isChecked) {
                filterStyles or DanmakuItem.Style.ROLLING
            } else {
                filterStyles and (DanmakuItem.Style.ROLLING.inv() and 0b111)
            }
            listener?.onDanmakuFilterStylesChanged(filterStyles)
        }

        binding.danmakuFontSizeSlider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.danmakuFontSizeText.text = when (value) {
                    1f -> "小"
                    2f -> "中"
                    3f -> "大"
                    else -> "未知"
                }
                listener?.onDanmakuFontSizeChanged(value.toInt())
            }
        }

        binding.danmakuVisibleRangeSlider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.danmakuVisibleRangeText.text = when (value) {
                    1f -> "1/4"
                    2f -> "1/2"
                    3f -> "3/4"
                    4f -> "全部"
                    else -> "未知"
                }
                listener?.onDanmakuVisibleRangeChanged(value.toInt())
            }
        }

        binding.danmakuSpeedSlider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.danmakuSpeedText.text = String.format(Locale.getDefault(), "%.1f", value)
                listener?.onDanmakuSpeedChanged(value)
            }
        }

        binding.danmakuTransparencySlider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.danmakuTransparencyText.text =
                    String.format(Locale.getDefault(), "%.1f", value / 255f)
                listener?.onDanmakuOpacityChanged(value.toInt())
            }
        }

        binding.videoPlaybackSpeedSlider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.videoPlaybackSpeedText.text =
                    String.format(Locale.getDefault(), "%.2f", value)
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