package com.arkueid.onair.domain.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * @author: Arkueid
 * @date: 2024/8/29
 * @desc:
 */
data class SearchTag(
    val content: String, val timestamp: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString() ?: "", parcel.readLong())

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(content)
        dest.writeLong(timestamp)
    }

    companion object CREATOR : Parcelable.Creator<SearchTag> {
        override fun createFromParcel(parcel: Parcel): SearchTag {
            return SearchTag(parcel)
        }

        override fun newArray(size: Int): Array<SearchTag?> {
            return arrayOfNulls(size)
        }
    }
}
