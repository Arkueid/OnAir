package com.arkueid.onair

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun stateTest() {
        val flow = flow {
            var data = 0
            while (true) {
                emit(requestData())
            }
        }

        runBlocking {
            flow.collect { data ->
                println("bind $data")
            }
        }
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