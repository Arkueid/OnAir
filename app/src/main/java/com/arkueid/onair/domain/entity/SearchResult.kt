package com.arkueid.onair.domain.entity

/**
 * @author: Arkueid
 * @date: 2024/8/29
 * @desc:
 */

typealias SearchTip = String
typealias SearchTipData = List<String>

data class SearchResult(
    val title: String,
    val cover: String,
)

typealias SearchResultData = List<SearchResult>