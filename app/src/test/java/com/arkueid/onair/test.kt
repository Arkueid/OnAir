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