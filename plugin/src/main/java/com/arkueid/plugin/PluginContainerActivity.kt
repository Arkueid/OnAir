package com.arkueid.plugin

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle

/**
 * @author: Arkueid
 * @date: 2024/9/6
 * @desc:
 */
class PluginContainerActivity : Activity() {
    private val pluginLoader = PluginLoader(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        pluginLoader.load()

        val clazz = classLoader.loadClass("com.arkueid.testsource.MainActivity")
        val constructor = clazz.getConstructor()
        val instance: IPlugin = constructor.newInstance() as IPlugin
        instance.attach(this)
        instance.onCreate(savedInstanceState)
    }

    override fun getResources(): Resources {
        return pluginLoader.resources
    }

    override fun getClassLoader(): ClassLoader {
        return pluginLoader.classLoader
    }


}