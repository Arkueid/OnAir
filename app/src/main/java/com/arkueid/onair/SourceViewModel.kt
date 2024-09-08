package com.arkueid.onair

import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkueid.onair.data.repository.Repository
import com.arkueid.onair.data.source.EmptySource
import com.arkueid.onair.event.SourceChangedEvent
import com.arkueid.plugin.PluginLoader
import com.arkueid.plugin.PluginLoaderManager
import com.arkueid.plugin.data.source.Source
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

/**
 * @author: Arkueid
 * @date: 2024/9/7
 * @desc:
 */
class SourceViewModel @Inject constructor(
    private val mmkv: MMKV,
    private val repository: Repository,
    private val okHttpClient: OkHttpClient
) : ViewModel() {

    companion object {
        private const val TAG = "SourceViewModel"
    }

    private val sourceChangedListeners = mutableListOf<() -> Unit>()
    fun addSourceChangedListener(listener: () -> Unit) {
        sourceChangedListeners.add(listener)
    }

    fun removeSourceChangedListener(listener: () -> Unit) {
        sourceChangedListeners.remove(listener)
    }

    private val loaders = mutableListOf<PluginLoader>()
    val plugins: List<PluginLoader> = loaders

    private val _icon = MutableLiveData<Any>()
    val icon: LiveData<Any> = _icon

    val allSources: List<Map<String, Any?>>
        get() = loaders.map {
            mapOf(
                "id" to it.id,
                "name" to it.name,
                "icon" to it.icon,
                "enabled" to (it.id == _currentSourceId)
            )
        }.toMutableList().apply {
            add(
                mapOf(
                    "id" to EmptySource::class.java.name,
                    "name" to "无数据源",
                    "icon" to R.drawable.heroine,
                    "enabled" to (_currentSourceId == EmptySource::class.java.name)
                )
            )
        }

    private var _currentSourceId: String

    init {
        _currentSourceId = mmkv.decodeString("current_source_id", EmptySource::class.java.name)!!
        loadPlugins()
    }

    fun changeSource(sourceId: String) {
        if (sourceId == _currentSourceId) return
        _currentSourceId = sourceId
        mmkv.encode("current_source_id", sourceId)
        setSource(sourceId)
    }

    private fun loadPlugins() {
        viewModelScope.launch {
            loaders.clear()
            withContext(Dispatchers.IO) {
                val pluginPath = Environment.getExternalStorageDirectory()
                pluginPath.listFiles()?.let { list ->
                    for (file in list) {
                        if (file.path.endsWith(".apk")) {
                            PluginLoaderManager.create(file.absolutePath)?.let {
                                loaders.add(it)
                            }
                        }
                    }
                }
            }
            setSource(_currentSourceId)
        }
    }

    private fun setSource(sourceId: String) {
        PluginLoaderManager.get(sourceId)?.let {
            repository.source = it.create<Source>(it.manifest.sourceId, okHttpClient)
        } ?: run {
            repository.source = null
        }
        _icon.value = PluginLoaderManager.get(sourceId)?.icon ?: R.drawable.heroine

        for (listener in sourceChangedListeners) {
            listener.invoke()
        }
    }
}