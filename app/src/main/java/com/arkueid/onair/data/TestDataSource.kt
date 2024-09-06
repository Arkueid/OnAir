package com.arkueid.onair.data

import android.util.Log
import com.arkueid.onair.common.timeString
import com.arkueid.onair.data.source.mikan.MikanSource
import com.arkueid.onair.domain.entity.Module
import com.arkueid.onair.domain.entity.Anime
import com.arkueid.onair.domain.entity.Danmaku
import com.arkueid.onair.domain.entity.SearchResult
import com.arkueid.onair.domain.entity.SearchTip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import kotlin.random.Random

/**
 * @author: Arkueid
 * @date: 2024/8/27
 * @desc:
 */
class TestDataSource(okHttpClient: OkHttpClient) : MikanSource(okHttpClient) {
    override val sourceId: String
        get() = "com.arkueid.source.test"
    override val sourceName: String
        get() = "测试数据源"

    private val url = "https://img.qunliao.info/4oEGX68t_9505974551.mp4"

    override fun getDanmakuData(anime: Anime): Flow<List<Danmaku>> {
        return flow {
            val ls = mutableListOf<Danmaku>()
            for (i in 0..300) {
                val time = Random.nextLong(0, 281924)
                val style = when (Random.nextInt(3)) {
                    0 -> Danmaku.Style.ROLLING
                    1 -> Danmaku.Style.TOP
                    else -> Danmaku.Style.BOTTOM
                }
                ls.add(
                    Danmaku(
                        "Danmaku-$time",
                        sourceId,
                        sourceName,
                        time,
                        "${anime.name}-${time.timeString}.${time % 1000}",
                        Random.nextInt(0xff0000, 0xffffff),
                        style,
                    )
                )
            }
            emit(ls.sortedBy { it.progress })
        }.flowOn(Dispatchers.IO)
    }

    override fun getModuleData(): Flow<List<Module>> {
        var time = System.currentTimeMillis()
        return flow {
            mutableListOf(
                Module(
                    Module.BANNER,
                    null,
                    List(3) { idx ->
                        Anime(
                            "",
                            sourceId,
                            "Banner",
                            sourceName,
                            "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                            "",
                            "",
                        )
                    },
                    ""
                )

            ).apply {
                addAll(fetchModulesFromOmofunIn())
                emit(this)
            }
        }.flowOn(Dispatchers.IO)
    }

    private val animeDatabase by lazy {
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

    override fun getSearchTipData(query: String): Flow<List<SearchTip>> {
        return flow {
            val keywords = query.split(" ").filter { it.isNotBlank() }
            val results = animeDatabase.filter { title ->
                keywords.all { keyword ->
                    title.contains(keyword, ignoreCase = true)
                }
            }
            emit(results) // 发射匹配到的结果
        }
    }

    override fun getSearchResultData(query: String): Flow<List<SearchResult>> {
        return getSearchTipData(query).transform {
            emit(it.mapIndexed { idx, tip ->
                SearchResult(
                    Anime(
                        "tip-$idx",
                        sourceId,
                        tip,
                        sourceName,
                        "https://acg.suyanw.cn/sjdm/random.php?r=${tip}",
                        url,
                        "简介：tip-$idx"
                    )
                )
            })
        }
    }

    private fun fetchModulesFromOmofunIn(): List<Module> {
        val req = Request.Builder().get().url("https://omofun.in/").build()
        val html = okHttpClient.newCall(req).execute().body()!!.string()
        return parseModules(html)
    }

    private fun parseModules(html: String): List<Module> {
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
        return elem.select(".module-items a").mapIndexed { idx, it ->
            Anime(
                "home-$it",
                sourceId,
                it.select(".module-poster-item-title").text(),
                sourceName,
                "https://omofun.in/${it.select(".module-item-pic img").attr("data-original")}",
                url,
                "简介：home-$idx"
            )
        }
    }

}