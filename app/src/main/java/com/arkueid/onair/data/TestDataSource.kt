package com.arkueid.onair.data

import com.arkueid.onair.data.source.mikan.MikanSource
import com.arkueid.onair.entity.ModuleData
import com.arkueid.onair.entity.Module
import com.arkueid.onair.entity.Anime
import com.arkueid.onair.entity.SearchResultItem
import com.arkueid.onair.entity.SearchResultData
import com.arkueid.onair.entity.SearchTipData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * @author: Arkueid
 * @date: 2024/8/27
 * @desc:
 */
class TestDataSource(okHttpClient: OkHttpClient) : MikanSource(okHttpClient) {

    private val url = "https://img.qunliao.info/4oEGX68t_9505974551.mp4"

    override fun getModuleData(): Flow<ModuleData> {
        var time = System.currentTimeMillis()
        return flow {
//            mutableListOf(
//                Module(
//                    Module.BANNER, null, listOf(
//                        ModuleItem(
//                            "",
//                            "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
//                        ), ModuleItem(
//                            "",
//                            "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
//                        ), ModuleItem(
//                            "",
//                            "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
//                        ), ModuleItem(
//                            "",
//                            "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
//                        )
//                    ), null
//                )
//            ).apply {
//                addAll(fetchModulesFromOmofunIn())
//                emit(this)
//            }
            emit(fetchHomeData())
        }.flowOn(Dispatchers.IO)
    }

    private val animes by lazy {
        listOf(
            "Naruto",
            "One Piece",
            "Bleach",
            "Attack on Titan",
            "Fullmetal Alchemist",
            "Dragon Ball Z",
            "Sword Art Online",
            "My Hero Academia",
            "Death Note",
            "Tokyo Ghoul",
            "Fairy Tail",
            "Hunter x Hunter",
            "Demon Slayer",
            "Jujutsu Kaisen",
            "Black Clover",
            "One Punch Man",
            "Gintama",
            "Blue Exorcist",
            "Soul Eater",
            "Fire Force",
            "Re:Zero",
            "No Game No Life",
            "Steins;Gate",
            "Cowboy Bebop",
            "Neon Genesis Evangelion",
            "Akame ga Kill",
            "Code Geass",
            "Fate/stay night",
            "KonoSuba",
            "Overlord",
            "The Rising of the Shield Hero",
            "The Promised Neverland",
            "Mob Psycho 100",
            "Parasyte",
            "Your Lie in April",
            "Clannad",
            "Angel Beats!",
            "Toradora!",
            "Anohana",
            "Violet Evergarden",
            "Madoka Magica",
            "Erased",
            "Tokyo Revengers",
            "Beastars",
            "Dr. Stone",
            "The Quintessential Quintuplets",
            "Kaguya-sama: Love is War",
            "Rent-a-Girlfriend",
            "The Seven Deadly Sins",
            "Noragami"
        )
    }

    override fun getSearchTipData(query: String): Flow<SearchTipData> {
        return flow {
            val keywords = query.split(" ").filter { it.isNotBlank() }
            val results = animes.filter { title ->
                keywords.all { keyword ->
                    title.contains(keyword, ignoreCase = true)
                }
            }
            emit(results) // 发射匹配到的结果
        }
    }

    override fun getSearchResultData(query: String): Flow<SearchResultData> {
        return getSearchTipData(query).transform {
            emit(it.map { tip ->
                SearchResultItem(
                    Anime(
                        tip,
                        "https://acg.suyanw.cn/sjdm/random.php?r=${tip}",
                        url,
                    )
                )
            })
        }
    }

    private fun fetchModulesFromOmofunIn(): ModuleData {
        val req = Request.Builder().get().url("https://omofun.in/").build()
        val html = okHttpClient.newCall(req).execute().body()!!.string()
        return parseModules(html)
    }

    private fun parseModules(html: String): ModuleData {
        val doc = Jsoup.parse(html)
        return doc.select(".module").mapIndexed { index, it ->
            val titleNodes = it.select(".module-title a").ifEmpty { it.select(".module-title") }
            Module(
                if (index != 0) Module.TALL_RECTANGLE_GRID else Module.WIDE_RECTANGLE_LIST,
                titleNodes.first()?.ownText(),
                parseModuleItems(it),
                "https://omofun.in/${it.select(".module-heading-more").attr("href")}"
            )
        }
    }

    private fun parseModuleItems(elem: Element): List<Anime> {
        return elem.select(".module-items a").map {
            Anime(
                it.select(".module-poster-item-title").text(),
                "https://omofun.in/${it.select(".module-item-pic img").attr("data-original")}",
                url,
            )
        }
    }

    private fun fetchHomeData(): ModuleData {
        var time: Long = 1
        return listOf(
            Module(
                Module.BANNER, null, listOf(
                    Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    )
                ), "https://www.google.com"
            ),
            Module(
                Module.WIDE_RECTANGLE_LIST, "热门推荐", listOf(
                    Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ),
                    Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ),
                    Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ),
                    Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ),
                    Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ),
                ), "example.com"
            ),
            Module(
                Module.TALL_RECTANGLE_GRID, "最新日漫", listOf(
                    Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(), "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    )
                ), "ssssss"
            ),
            Module(
                Module.TALL_RECTANGLE_GRID, "最新国漫", listOf(
                    Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    )
                ), "ssss"
            ),
            Module(
                Module.TALL_RECTANGLE_GRID, "最新美漫", listOf(
                    Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    )
                ), "ssss"
            ),
            Module(
                Module.TALL_RECTANGLE_GRID, "最新剧场", listOf(
                    Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    ), Anime(
                        "${time++}".reversed(),
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        url,
                    )
                ), "ssss"
            ),
        )
    }

}