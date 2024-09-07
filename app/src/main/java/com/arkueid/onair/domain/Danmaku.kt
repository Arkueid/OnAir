package com.arkueid.onair.domain

import com.arkueid.plugin.data.entity.Danmaku
import com.arkueid.plugin.data.entity.Danmaku.Style
import com.arkueid.plugin.data.entity.IDanmaku

/**
 * @author: Arkueid
 * @date: 2024/9/7
 * @desc:
 */
fun Danmaku.toDisplay(): DisplayDanmaku = DisplayDanmaku(this)

/**
 * Danmaku for rendering
 */
class DisplayDanmaku(item: Danmaku) : IDanmaku by item {

    // danmaku item rendering params
    var width: Float = Float.NEGATIVE_INFINITY

    var x: Float = Float.NEGATIVE_INFINITY

    var trackId: Int = Int.MIN_VALUE

    var speed: Float = 0.0f
}

val DisplayDanmaku.isRolling: Boolean get() = style == Style.ROLLING
val DisplayDanmaku.isTop: Boolean get() = style == Style.TOP
val DisplayDanmaku.isBottom: Boolean get() = style == Style.BOTTOM