package com.arkueid.onair.ui.play.danmaku

import com.arkueid.onair.entity.DanmakuItem

/**
 * Danmaku for rendering
 */
class DisplayDanmaku(progress: Long, content: String, color: Int, style: Int) :
    DanmakuItem(progress, content, color, style) {

    constructor(item: DanmakuItem) : this(item.progress, item.content, item.color, item.style)

    // danmaku item rendering params
    var width: Float = Float.NEGATIVE_INFINITY

    var x: Float = Float.NEGATIVE_INFINITY

    var trackId: Int = Int.MIN_VALUE

    var speed: Float = 0.0f

    var isSelected: Boolean = false

    var selectedAt: Long = 0
}

fun DanmakuItem.toDisplay(): DisplayDanmaku = DisplayDanmaku(this)

fun DisplayDanmaku.toItem(): DanmakuItem = this