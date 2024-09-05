package com.arkueid.onair.domain.entity

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.arkueid.onair.domain.display.DisplayDanmaku
import com.arkueid.onair.ui.play.PlayerActivity
import kotlinx.parcelize.Parcelize

/**
 * @author: Arkueid
 * @date: 2024/8/26
 * @desc: anime item model
 */
interface IAnime {
    val title: String
    val cover: String
    val url: String
}

@Parcelize
data class Anime(
    override val title: String,
    override val cover: String,
    override val url: String,
) : Parcelable, IAnime

fun Danmaku.toDisplay(): DisplayDanmaku = DisplayDanmaku(this)

fun Anime.play(context: Context) {
    val intent = Intent(context, PlayerActivity::class.java)
    intent.putExtra("anime", this)
    context.startActivity(intent)
}