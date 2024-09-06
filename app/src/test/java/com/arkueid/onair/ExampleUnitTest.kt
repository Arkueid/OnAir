package com.arkueid.onair

import com.arkueid.onair.domain.entity.DanmakuImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.junit.Test
import kotlin.random.Random

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val l by lazy {
        List(500) {
            val p = Random.nextLong(0, 5000)
            DanmakuImpl(
                progress = p, // 时间范围 0 到 7分27秒
                content = "弹幕$p",
                color = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt()) // 随机颜色
            )
        }
    }
    @Test
    fun stateTest() {


        println(l)
    }

    private var data = 0

    private suspend fun requestData(): Int {
        withContext(Dispatchers.IO) {
            data += 1
            delay(100)
        }
        return data
    }
}