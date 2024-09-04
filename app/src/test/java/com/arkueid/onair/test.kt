package com.arkueid.onair

/**
 * @author: Arkueid
 * @date: 2024/8/28
 * @desc:
 */

data class Anime(
    val title: String,
    val cover: String,
    val url: String
)

fun main() {
    val l = listOf(Anime("title", "cove22r", "url"), Anime("title", "cover", "url"))
    println(
        l.contains(Anime("title", "cove22r", "url"))
    )
}