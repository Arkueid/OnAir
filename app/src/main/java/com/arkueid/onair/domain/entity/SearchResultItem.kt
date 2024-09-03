package com.arkueid.onair.domain.entity

/**
 * @author: Arkueid
 * @date: 2024/8/29
 * @desc:
 */

typealias SearchTip = String
typealias SearchTipData = List<SearchTip>

data class SearchResultItem(
    val anime: Anime,
)

typealias SearchResultData = List<SearchResultItem>