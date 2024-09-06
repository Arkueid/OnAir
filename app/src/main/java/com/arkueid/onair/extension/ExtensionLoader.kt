package com.arkueid.onair.extension

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

/**
 * @author: Arkueid
 * @date: 2024/9/5
 * @desc:
 */

object ExtensionLoader {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        this.applicationContext = context
    }

    private fun isExtension(packageInfo: PackageInfo): Boolean {
        return packageInfo.applicationInfo.metaData.getBoolean("onair_extension")
    }

    fun load(apkPath: String): ExtensionInfo? {
        val pm = applicationContext.packageManager
        val packageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_META_DATA)

        if (packageInfo != null && isExtension(packageInfo)) {
            packageInfo.let {
                return ExtensionInfo(
                    packageInfo.packageName,
                    packageInfo.versionName,
                    packageInfo.applicationInfo.loadLabel(pm).toString(),
                    pm.getApplicationIcon(packageInfo.applicationInfo)
                )
            }
        }

        return null
    }
}