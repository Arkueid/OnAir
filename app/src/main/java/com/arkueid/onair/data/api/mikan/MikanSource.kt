package com.arkueid.onair.data.api.mikan

import com.arkueid.onair.data.repository.DataSource
import com.arkueid.onair.ui.weekly.model.WeeklyDataHolder
import com.arkueid.onair.ui.weekly.model.WeeklySubjectHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


class MikanSource(private val okHttpClient: OkHttpClient) : DataSource {

    companion object {
        private const val BASE_URL = "https://mikanime.tv/"
    }

    // scraping weekly anime update info from html
    override fun getWeeklyData(): Flow<WeeklyDataHolder> {
        return flow {
            val html = Request.Builder().url(BASE_URL).get().build().let {
                okHttpClient.newCall(it).execute().body()!!.string()
            }
            emit(parseWeeklyData(html))
        }.flowOn(Dispatchers.IO)
    }

    private fun parseWeeklyData(html: String): WeeklyDataHolder {
        val doc = Jsoup.parse(html)
        return WeeklyDataHolder(doc.select(".m-home-week-item")
            .take(7)
            .reversed()
            .mapIndexed { index, elemByWeekDay ->
                parseWeeklySubject(index, elemByWeekDay)
            })
    }

    private fun parseWeeklySubject(index: Int, element: Element): List<WeeklySubjectHolder> {
        return element.select(".m-week-square").map {
            WeeklySubjectHolder(
                it.select(".small-title").text(),
                it.select(".b-lazy").attr("data-src").let { "$BASE_URL$it" },
                index + 1
            )
        }
    }
}