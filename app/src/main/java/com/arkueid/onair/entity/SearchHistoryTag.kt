package com.arkueid.onair.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * @author: Arkueid
 * @date: 2024/8/29
 * @desc:
 */
data class SearchHistoryTag(
    val content: String, val timestamp: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString() ?: "", parcel.readLong())

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(content)
        dest.writeLong(timestamp)
    }

    companion object CREATOR : Parcelable.Creator<SearchHistoryTag> {
        override fun createFromParcel(parcel: Parcel): SearchHistoryTag {
            return SearchHistoryTag(parcel)
        }

        override fun newArray(size: Int): Array<SearchHistoryTag?> {
            return arrayOfNulls(size)
        }
    }
}
