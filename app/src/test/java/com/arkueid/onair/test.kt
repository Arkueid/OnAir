package com.arkueid.onair

/**
 * @author: Arkueid
 * @date: 2024/8/28
 * @desc:
 */

interface IAnime {
    val title: String
    val cover: String
    val url: String

    val id get() = this.javaClass.name
}

data class Anime(
    override val title: String,
    override val cover: String,
    override val url: String
): IAnime

fun main() {
    val anime : IAnime = Anime("title", "cover", "url")
    println(anime.id)
}