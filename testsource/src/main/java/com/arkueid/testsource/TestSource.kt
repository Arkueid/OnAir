package com.arkueid.testsource


import com.arkueid.plugin.data.entity.Anime
import com.arkueid.plugin.data.entity.Danmaku
import com.arkueid.plugin.data.entity.Module
import com.arkueid.plugin.data.entity.SearchResult
import com.arkueid.plugin.data.entity.SearchTip
import com.arkueid.testsource.mikan.MikanSource
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
val Long.timeString: String
    get() {
        val hour = this / 3600000
        val minute = this % 3600000 / 60000
        val second = this % 60000 / 1000
        return if (hour > 0) {
            "%02d:%02d:%02d".format(hour, minute, second)
        } else {
            "%02d:%02d".format(minute, second)
        }
    }

class TestSource(okHttpClient: OkHttpClient) : MikanSource(okHttpClient) {
    override val sourceId: String
        get() = "com.arkueid.source.test"
    override val sourceName: String
        get() = "测试数据源"

    val url = "https://img.qunliao.info/4oEGX68t_9505974551.mp4"

    override fun getDanmakuData(anime: Anime): List<Danmaku> {
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
        return ls.sortedBy { it.progress }

    }

    override fun getModuleData(): List<Module> {
        var time = System.currentTimeMillis()
        return mutableListOf(
            Module(
                Module.BANNER, null, List(3) {
                    Anime(
                        "",
                        sourceId,
                        "",
                        sourceName,
                        "https://acg.suyanw.cn/sjdm/random.php?r=${time++}",
                        "",
                        "",
                    )
                }, ""
            )

        ).apply {
            addAll(fetchModulesFromOmofunIn())
        }
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

    override fun getSearchTipData(query: String): List<SearchTip> {
        val keywords = query.split(" ").filter { it.isNotBlank() }
        val results = animeDatabase.filter { title ->
            keywords.all { keyword ->
                title.contains(keyword, ignoreCase = true)
            }
        }
        return results
    }

    override fun getSearchResultData(query: String): List<SearchResult> {
        return getSearchTipData(query).mapIndexed { idx, tip ->
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
        }
    }
}

private fun TestSource.fetchModulesFromOmofunIn(): List<Module> {
    val req = Request.Builder().get().url("https://omofun.in/").build()
    val html = okHttpClient.newCall(req).execute().body()!!.string()
    return parseModules(html)
}

private fun TestSource.parseModules(html: String): List<Module> {
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

private fun TestSource.parseModuleItems(elem: Element): List<Anime> {
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
