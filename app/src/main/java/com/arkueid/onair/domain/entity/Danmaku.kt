package com.arkueid.onair.domain.entity

import com.arkueid.onair.domain.entity.Danmaku.Style

private interface IDanmaku {
    val progress: Long
    val content: String
    val color: Int
    val style: Int
}

data class Danmaku(
    val id: String,
    val sourceId: String,
    val sourceName: String,
    override val progress: Long,
    override val content: String,
    override val color: Int,
    override val style: Int,
) : IDanmaku {
    object Style {
        const val ROLLING = 0b001
        const val TOP = 0b010
        const val BOTTOM = 0b100
    }
}

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