package com.arkueid.plugin.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author: Arkueid
 * @date: 2024/8/26
 * @desc: anime item model
 */
@Parcelize
data class Anime(
    // 确定唯一性
    val id: String,
    val sourceId: String,

    // 基本信息
    val name: String,
    val sourceName: String,
    val cover: String?,
    val url: String,
    val intro: String?,
) : Parcelable
