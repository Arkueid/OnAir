package com.arkueid.onair.data.source.mikan

import com.arkueid.onair.data.source.DataSource
import com.arkueid.onair.domain.ModuleData
import com.arkueid.onair.domain.SearchResult
import com.arkueid.onair.domain.SearchTipData
import com.arkueid.onair.domain.WeeklyData
import com.arkueid.onair.domain.entity.Anime
import com.arkueid.onair.domain.entity.WeeklyAnime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


open class MikanSource(protected val okHttpClient: OkHttpClient) : DataSource {

    companion object {
        private const val BASE_URL = "https://mikanime.tv/"
    }

    // scraping weekly anime update info from html
    override fun getWeeklyData(): Flow<WeeklyData> {
        return flow {
            val html = Request.Builder().url(BASE_URL).get().build().let {
                okHttpClient.newCall(it).execute().body()!!.string()
            }
            emit(parseWeeklyData(html))
        }.flowOn(Dispatchers.IO)
    }

    override fun getModuleData(): Flow<ModuleData> {
        TODO("Not yet implemented")
    }

    override fun getSearchTipData(query: String): Flow<SearchTipData> {
        TODO("Not yet implemented")
    }

    override fun getSearchResultData(query: String): Flow<SearchResult> {
        TODO("Not yet implemented")
    }

    private fun parseWeeklyData(html: String): WeeklyData {
        val doc = Jsoup.parse(html)
        return doc.select(".m-home-week-item").take(7)
            .sortedBy { text2DayOfWeek(it.select(".title span").text()) }
            .mapIndexed { index, elemByWeekDay ->
                parseWeeklySubject(index, elemByWeekDay)
            }
    }

    private fun parseWeeklySubject(index: Int, element: Element): List<WeeklyAnime> {
        return element.select(".m-week-square").map {
            WeeklyAnime(
                Anime(
                    it.select(".small-title").text(),
                    it.select(".b-lazy").attr("data-src").let { s -> "$BASE_URL$s" },
                    "https://img.qunliao.info/4oEGX68t_9505974551.mp4", // TODO
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