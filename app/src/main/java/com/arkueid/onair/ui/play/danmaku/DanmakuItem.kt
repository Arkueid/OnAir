package com.arkueid.onair.ui.play.danmaku


class DanmakuItem(
    val progress: Long,
    val content: String,
    val color: Int,
    val style: Int = Style.ROLLING
) {


     object Style {
        const val ROLLING = 0b001
        const val TOP = 0b010
        const val BOTTOM = 0b100
    }

    var width: Float = Float.NEGATIVE_INFINITY

    var x: Float = Float.NEGATIVE_INFINITY

    var trackId: Int = Int.MIN_VALUE

    var speed: Float = 0.0f

    var skip: Boolean = false

    var isSelected: Boolean = false

    var selectedAt: Long = 0

    // 根据进度计算弹幕所在位置
    // 默认30fps
    fun syncX(currentProgress: Long, interval: Float = 1000f / 30f) {
        x -= speed * (currentProgress - progress) / interval
    }
}
