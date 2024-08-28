package com.arkueid.onair.utils

/**
 * @author: Arkueid
 * @date: 2024/8/28
 * @desc:
 */
class Result<out T>(
    val isSuccess: Boolean,
    val data: T?,
    val error: Throwable?,
) {

    val isFailure = !isSuccess

    companion object {
        fun <T> success(data: T): Result<T> = Result(true, data, null)

        fun <T> failure(error: Throwable, cached: T?): Result<T> = Result(false, cached, error)
    }
}