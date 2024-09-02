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

object Style {
    const val ROLLING = 0b001
    const val TOP = 0b010
    const val BOTTOM = 0b100
}

fun main() {
    var styles = 0

    styles = styles or Style.TOP
    styles = styles or Style.BOTTOM
    styles = styles or Style.ROLLING
    println(styles)
    styles = styles and (Style.ROLLING.inv() and 0b111)
    println(styles)
    println(styles and (Style.TOP.inv() and 0b111))


}