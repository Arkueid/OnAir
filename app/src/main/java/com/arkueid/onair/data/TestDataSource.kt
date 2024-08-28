package com.arkueid.onair.data

import com.arkueid.onair.data.api.mikan.MikanSource
import com.arkueid.onair.domain.entity.ModuleDataHolder
import com.arkueid.onair.domain.entity.Module
import com.arkueid.onair.domain.entity.ModuleItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient

/**
 * @author: Arkueid
 * @date: 2024/8/27
 * @desc:
 */
class TestDataSource(okHttpClient: OkHttpClient) : MikanSource(okHttpClient) {

    override fun getModuleData(): Flow<ModuleDataHolder> {
        return flow { emit(fetchHomeData()) }
    }

    private fun fetchHomeData(): ModuleDataHolder {
        var time: Long = 1
        return listOf(
            Module(
                Module.BANNER,
                null,
                listOf(
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    )
                ),
                "https://www.google.com"
            ),
            Module(
                Module.WIDE_RECTANGLE_LIST,
                "热门推荐",
                listOf(
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                ),
                "example.com"
            ),
            Module(
                Module.TALL_RECTANGLE_GRID,
                "最新日漫",
                listOf(
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}"
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}"
                    )
                ),
                "ssssss"
            ),
            Module(
                Module.TALL_RECTANGLE_GRID,
                "最新国漫",
                listOf(
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}"
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}"
                    )
                ),
                "ssss"
            ),
            Module(
                Module.TALL_RECTANGLE_GRID,
                "最新美漫",
                listOf(
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}"
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}"
                    )
                ),
                "ssss"
            ),
            Module(
                Module.TALL_RECTANGLE_GRID,
                "最新剧场",
                listOf(
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}"
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                    ),
                    ModuleItem(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}"
                    )
                ),
                "ssss"
            ),
        )
    }

}