package com.arkueid.onair.data.api.mikan

import com.arkueid.onair.data.repository.DataSource
import com.arkueid.onair.domain.entity.ModuleDataHolder
import com.arkueid.onair.domain.entity.WeeklyData
import com.arkueid.onair.domain.entity.WeeklySubject
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

    override fun getModuleData(): Flow<ModuleDataHolder> {
        TODO("Not yet implemented")
    }

    private fun parseWeeklyData(html: String): WeeklyData {
        val doc = Jsoup.parse(html)
        return doc.select(".m-home-week-item")
            .take(7)
            .sortedBy { text2DayOfWeek(it.select(".title span").text()) }
            .mapIndexed { index, elemByWeekDay ->
                parseWeeklySubject(index, elemByWeekDay)
            }
    }

    private fun parseWeeklySubject(index: Int, element: Element): List<WeeklySubject> {
        return element.select(".m-week-square").map {
            WeeklySubject(
                it.select(".small-title").text(),
                it.select(".b-lazy").attr("data-src").let { "$BASE_URL$it" },
                index + 1
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