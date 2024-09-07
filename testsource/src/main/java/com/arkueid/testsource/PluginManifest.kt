package com.arkueid.testsource

import com.arkueid.plugin.IPluginManifest

/**
 * @author: Arkueid
 * @date: 2024/9/7
 * @desc:
 */
class PluginManifest : IPluginManifest {
    override val settingsActivityId: String?
        get() = MainActivity::class.java.name
    override val sourceId: String
        get() = TestSource::class.java.name
}