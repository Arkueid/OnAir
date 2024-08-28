package com.arkueid.onair

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * @author: Arkueid
 * @date: 2024/8/28
 * @desc:
 */
fun main() {
    val flow = MutableStateFlow(0)

    CoroutineScope(Dispatchers.IO).launch {
        flow.collectLatest {
            println("bind2 $data")
        }
    }

    runBlocking {
        flow.update {
            10
        }
        delay(1000)
    }
}

private var data = 0

private suspend fun requestData(): Int {
    withContext(Dispatchers.IO) {
        data += 1
    }
    return data
}