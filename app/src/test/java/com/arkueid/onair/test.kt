package com.arkueid.onair

/**
 * @author: Arkueid
 * @date: 2024/8/28
 * @desc:
 */

class A {
    fun s() {}
}

val list = mutableListOf<() -> Unit>()

fun main() {
    val a = A()
    list.add(a::s)

    list.remove(a::s)

    println(list.size)
}