package com.arkueid.onair.domain.store

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author: Arkueid
 * @date: 2024/9/6
 * @desc:
 */

@Parcelize
data class StoreAnime(val title: String, val cover: String) : Parcelable
