package com.arkueid.onair

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
    val s = "clan"

    CoroutineScope(Dispatchers.Default).launch {
        query
            .debounce(500)
            .distinctUntilChanged()
            .flatMapLatest {
                search(s)
            }.collect { data ->
                tips.update { data }
            }
    }

    CoroutineScope(Dispatchers.Default).launch {
        tips.collectLatest {
            println(it)
        }
    }

    query.value = s

    Thread.sleep(3000)
}