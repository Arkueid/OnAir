package com.arkueid.plugin.data.entity

interface IDanmaku {
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