package com.arkueid.onair.domain.display

import com.arkueid.onair.domain.entity.Danmaku
import com.arkueid.onair.domain.entity.IDanmaku

/**
 * Danmaku for rendering
 */
class DisplayDanmaku(private val item: Danmaku) : IDanmaku by item {

    // danmaku item rendering params
    var width: Float = Float.NEGATIVE_INFINITY

    var x: Float = Float.NEGATIVE_INFINITY

    var trackId: Int = Int.MIN_VALUE

    var speed: Float = 0.0f

    var isSelected: Boolean = false

    var selectedAt: Long = 0

    fun toItem(): Danmaku = item
}