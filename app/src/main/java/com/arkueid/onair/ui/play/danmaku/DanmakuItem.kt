package com.arkueid.onair.ui.play.danmaku


private const val UPDATE_INTERVAL = 1000f / 30f

class DanmakuItem(
    val progress: Long, val content: String, val color: Int
) {

    // params for rolling danmaku, not for initialization
    var width: Float = Float.NEGATIVE_INFINITY

    var x: Float = Float.NEGATIVE_INFINITY

    var y: Float = Float.NEGATIVE_INFINITY

    var speed: Float = 0.0f

    fun updatePosition(currentProgress: Long) {
        x -= speed * (currentProgress - progress) / UPDATE_INTERVAL
    }
}
