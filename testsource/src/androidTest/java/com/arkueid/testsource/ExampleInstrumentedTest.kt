package com.arkueid.testsource

import android.content.pm.PackageManager
import android.os.Environment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val list = Environment.getExternalStorageDirectory().listFiles()
        if (!list.isNullOrEmpty()) for (file in list) {
            appContext.packageManager.getPackageArchiveInfo(
                file.absolutePath, PackageManager.GET_META_DATA
            )?.let {
                println(it.packageName)
            }
        }
    }
}