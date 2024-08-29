package com.arkueid.onair

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkueid.onair.data.TestDataSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    companion object {
        private const val TAG = "ExampleInstrumentedTest"
    }

    @Test
    fun useAppContext() {

        runBlocking {
            TestDataSource(OkHttpClient()).getSearchTipData("clan").collect {
                Log.d(TAG, "useAppContext: $it")
            }
        }
    }
}