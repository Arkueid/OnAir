package com.arkueid.onair.domain.entity

import com.arkueid.onair.common.timeString
import com.arkueid.onair.domain.entity.Danmaku.Style
import kotlin.random.Random

interface IDanmaku {
    val progress: Long
    val content: String
    val color: Int
    val style: Int
}

val IDanmaku.isRolling: Boolean get() = style == Style.ROLLING
val IDanmaku.isTop: Boolean get() = style == Style.TOP
val IDanmaku.isBottom: Boolean get() = style == Style.BOTTOM

data class Danmaku(
    override val progress: Long,
    override val content: String,
    override val color: Int,
    override val style: Int = Style.ROLLING
) : IDanmaku {
    companion object {}

    object Style {
        const val ROLLING = 0b001
        const val TOP = 0b010
        const val BOTTOM = 0b100
    }
}

// for test
fun Danmaku.Companion.testData(duration: Long): List<Danmaku> {
    val list = mutableListOf<Danmaku>()
    val styles = listOf(Danmaku.Style.ROLLING, Danmaku.Style.TOP, Danmaku.Style.BOTTOM)
    for (i in 0..1200) {
        val p = Random.nextLong(0, duration)
        list.add(
            Danmaku(
                progress = p, // 时间范围 0 到 7分27秒
                content = "弹幕${p.timeString}.${p % 1000}",
                color = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt()), // 随机颜色
                style = styles.random()
            )
        )
    }
    return list.apply { sortBy { it.progress } }
}