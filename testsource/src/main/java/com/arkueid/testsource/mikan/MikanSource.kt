package com.arkueid.testsource.mikan

import com.arkueid.plugin.data.entity.Anime
import com.arkueid.plugin.data.entity.Danmaku
import com.arkueid.plugin.data.entity.Module
import com.arkueid.plugin.data.entity.SearchResult
import com.arkueid.plugin.data.entity.SearchTip
import com.arkueid.plugin.data.entity.WeeklyAnime
import com.arkueid.plugin.data.source.Source
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


open class MikanSource(val okHttpClient: OkHttpClient) : Source {

    override val sourceId: String
        get() = "com.arkueid.source.mikan"

    override val sourceName: String
        get() = "Mikan"

    companion object {
        private const val BASE_URL = "https://mikanime.tv/"
    }

    // scraping weekly anime update info from html
    override fun getWeeklyData(): List<List<WeeklyAnime>> {
        val html = Request.Builder().url(BASE_URL).get().build().let {
            okHttpClient.newCall(it).execute().body()!!.string()
        }
        return parseWeeklyData(html)
    }

    override fun getModuleData(): List<Module> {
        TODO("Not yet implemented")
    }

    override fun getSearchTipData(query: String): List<SearchTip> {
        TODO("Not yet implemented")
    }

    override fun getSearchResultData(query: String): List<SearchResult> {
        TODO("Not yet implemented")
    }

    override fun getDanmakuData(anime: Anime): List<Danmaku> {
        TODO("Not yet implemented")
    }

    private fun parseWeeklyData(html: String): List<List<WeeklyAnime>> {
        val doc = Jsoup.parse(html)
        return doc.select(".m-home-week-item").take(7)
            .sortedBy { text2DayOfWeek(it.select(".title span").text()) }
            .mapIndexed { index, elemByWeekDay ->
                parseWeeklySubject(index, elemByWeekDay)
            }
    }

    private fun parseWeeklySubject(index: Int, element: Element): List<WeeklyAnime> {
        return element.select(".m-week-square").mapIndexed { id, it ->
            WeeklyAnime(
                Anime(
                    "weekly-$id",
                    sourceId,
                    it.select(".small-title").text(),
                    sourceName,
                    it.select(".b-lazy").attr("data-src").let { s -> "$BASE_URL$s" },
                    "https://img.qunliao.info/4oEGX68t_9505974551.mp4", // TODO
                    "简介：",
                ), index + 1
            )
        }
    }

    private fun text2DayOfWeek(text: String): Int {
        return when (text) {
            "星期一" -> 1
            "星期二" -> 2
            "星期三" -> 3
            "星期四" -> 4
            "星期五" -> 5
            "星期六" -> 6
            "星期日" -> 7
            else -> throw IllegalArgumentException("Unknown day of week: $text")
        }
    }
}