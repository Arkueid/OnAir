package com.arkueid.onair.data.source.bangumi.model.weekly

data class Item(
    val air_date: String,
    val air_weekday: Int,
    val collection: Collection,
    val eps: Int,
    val eps_count: Int,
    val id: Int,
    val images: Images,
    val name: String,
    val name_cn: String,
    val rank: Int,
    val rating: Rating,
    val summary: String,
    val type: Int,
    val url: String
)