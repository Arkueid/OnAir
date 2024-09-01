package com.arkueid.onair

import com.arkueid.onair.ui.play.danmaku.DanmakuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import kotlin.random.Random

/**
 * @author: Arkueid
 * @date: 2024/8/28
 * @desc:
 */

val animes = listOf(
    "Naruto", "One Piece", "Bleach", "Attack on Titan", "Fullmetal Alchemist",
    "Dragon Ball Z", "Sword Art Online", "My Hero Academia", "Death Note",
    "Tokyo Ghoul", "Fairy Tail", "Hunter x Hunter", "Demon Slayer",
    "Jujutsu Kaisen", "Black Clover", "One Punch Man", "Gintama", "Blue Exorcist",
    "Soul Eater", "Fire Force", "Re:Zero", "No Game No Life", "Steins;Gate",
    "Cowboy Bebop", "Neon Genesis Evangelion", "Akame ga Kill", "Code Geass",
    "Fate/stay night", "KonoSuba", "Overlord", "The Rising of the Shield Hero",
    "The Promised Neverland", "Mob Psycho 100", "Parasyte", "Your Lie in April",
    "Clannad", "Angel Beats!", "Toradora!", "Anohana", "Violet Evergarden",
    "Madoka Magica", "Erased", "Tokyo Revengers", "Beastars", "Dr. Stone",
    "The Quintessential Quintuplets", "Kaguya-sama: Love is War",
    "Rent-a-Girlfriend", "The Seven Deadly Sins", "Noragami"
)

val query = MutableStateFlow("")

val tips = MutableStateFlow(emptyList<String>())

fun search(query: String): Flow<List<String>> {
    return flow {
        emit(animes.filter {
            it.contains(it, ignoreCase = true)
        })
    }.flowOn(Dispatchers.IO)
}

fun main() {
    val l by lazy {
        List(500) {
            val p = Random.nextLong(0, 5000)
            DanmakuItem(
                progress = p, // 时间范围 0 到 7分27秒
                content = "弹幕$p",
                color = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt()) // 随机颜色
            )
        }
    }
    println(l)
}