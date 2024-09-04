package com.arkueid.onair.entity

open class DanmakuItem(
    val progress: Long, val content: String, val color: Int, val style: Int = Style.ROLLING
) {

    object Style {
        const val ROLLING = 0b001
        const val TOP = 0b010
        const val BOTTOM = 0b100
    }
}

fun DanmakuItem.isRolling() = style == DanmakuItem.Style.ROLLING
fun DanmakuItem.isTop() = style == DanmakuItem.Style.TOP
fun DanmakuItem.isBottom() = style == DanmakuItem.Style.BOTTOM