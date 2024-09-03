package com.arkueid.onair.domain.entity

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import com.arkueid.onair.ui.play.PlayerActivity
import kotlinx.parcelize.Parcelize

/**
 * @author: Arkueid
 * @date: 2024/8/26
 * @desc: anime item model
 */
@Parcelize
data class Anime(
    val title: String,
    val cover: String,
    val url: String,
    var liked: Boolean = false
) : Parcelable

fun Anime.play(context: Context) {
    val intent = Intent(context, PlayerActivity::class.java)
    intent.putExtra("anime", this)
    context.startActivity(intent)
}