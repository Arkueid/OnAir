package com.arkueid.onair.data.entity

import android.content.Context
import android.content.Intent
import com.arkueid.onair.ui.play.PlayerActivity
import com.arkueid.plugin.data.entity.Anime

/**
 * @author: Arkueid
 * @date: 2024/9/7
 * @desc:
 */
fun Anime.play(context: Context) {
    val intent = Intent(context, PlayerActivity::class.java)
    intent.putExtra("anime", this)
    context.startActivity(intent)
}