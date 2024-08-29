package com.arkueid.onair.domain.entity

/**
 * @author: Arkueid
 * @date: 2024/8/26
 * @desc: module display model for home page
 */
data class Module(
    val style: Int,
    val title: String?,
    val items: List<ModuleItem>,
    // if data source provides more url, use it to jump to more page
    val moreUrl: String?,
) {

    companion object {
        // module style
        const val BANNER = 0

        // grid style
        // grid style is applied only when the module has 6 or more items
        const val SQUARE_GRID = 1
        const val TALL_RECTANGLE_GRID = 2
        const val WIDE_RECTANGLE_GRID = 3

        // list style
        const val SQUARE_LIST = 4
        const val TALL_RECTANGLE_LIST = 5
        const val WIDE_RECTANGLE_LIST = 6
    }
}

typealias ModuleData = List<Module>
