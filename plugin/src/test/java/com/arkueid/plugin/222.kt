package com.arkueid.plugin

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

/**
 * @author: Arkueid
 * @date: 2024/9/8
 * @desc:
 */
fun main() {
    var source1 = 123
    var source2 = 456
    var source = source1
    val flow = flow<Int> {
        emit(source)
    }

    source = source2
    runBlocking {
        flow.collect {
            println(it)
        }
    }
}