package com.arkueid.onair

import androidx.lifecycle.ViewModel
import com.arkueid.onair.data.repository.Repository
import com.arkueid.onair.data.source.EmptySource
import com.arkueid.onair.event.SourceChangedEvent
import com.arkueid.plugin.PluginLoaderManager
import com.arkueid.plugin.data.source.Source
import com.tencent.mmkv.MMKV
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

/**
 * @author: Arkueid
 * @date: 2024/9/7
 * @desc:
 */
class SourceViewModel @Inject constructor(
    private val mmkv: MMKV,
    private val repository: Repository
) : ViewModel() {

    private val sources = mutableMapOf<String, Source>()

    private var _currentSourceId: String = EmptySource::class.java.name

    val currentSource: Source
        get() = sources[_currentSourceId] ?: repository.defaultSource

    var currentSourceId: String
        get() = _currentSourceId
        set(value) {
            _currentSourceId = value
            repository.source = sources[value]
            mmkv.encode("current_source", value)
            EventBus.getDefault().removeAllStickyEvents()
            EventBus.getDefault().postSticky(SourceChangedEvent())
        }

    init {
        sources[repository.defaultSource.sourceId] = repository.defaultSource
        currentSourceId = mmkv.decodeString("current_source") ?: repository.defaultSource.sourceId
    }

    fun register(pluginId: String): Boolean {
        if (sources.containsKey(pluginId)) return true
        val loader = PluginLoaderManager.get(pluginId)
        loader.create<Source>(loader.manifest.sourceId)?.let {
            sources[pluginId] = it
        }
        return sources.containsKey(pluginId)
    }

    fun unregister(pluginId: String) {
        if (_currentSourceId == pluginId) currentSourceId = repository.defaultSource.sourceId
        sources.remove(pluginId)
    }
}