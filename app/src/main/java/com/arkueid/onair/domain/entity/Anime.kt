package com.arkueid.onair.domain.entity

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import com.arkueid.onair.ui.play.PlayerActivity
import java.io.Serializable

/**
 * @author: Arkueid
 * @date: 2024/8/26
 * @desc: anime item model
 */
data class Anime(
    val title: String,
    val cover: String,
    val url: String,
) : Parcelable {

    var liked: Boolean = false

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    ) {
        liked = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(cover)
        parcel.writeString(url)
        parcel.writeByte(if (liked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Anime> {
        override fun createFromParcel(parcel: Parcel): Anime {
            return Anime(parcel)
        }

        override fun newArray(size: Int): Array<Anime?> {
            return arrayOfNulls(size)
        }
    }
}

fun Anime.play(context: Context) {
    val intent = Intent(context, PlayerActivity::class.java)
    intent.putExtra("anime", this)
    context.startActivity(intent)
}