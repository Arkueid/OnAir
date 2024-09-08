package com.arkueid.onair.ui.plugin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arkueid.onair.databinding.ItemPluginBinding
import com.arkueid.plugin.PluginLoader

/**
 * @author: Arkueid
 * @date: 2024/9/7
 * @desc:
 */
class PluginAdapter(
    var data: List<PluginLoader>,
    private val onSettingsClicked: ((PluginLoader) -> Unit)?
) : RecyclerView.Adapter<PluginAdapter.PluginViewHolder>() {

    class PluginViewHolder(binding: ItemPluginBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.name
        val version = binding.versionName
        val icon = binding.icon
        val settings = binding.settings
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PluginViewHolder {
        return PluginViewHolder(
            ItemPluginBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PluginViewHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.name
        holder.version.text = item.versionName
        holder.icon.setImageDrawable(item.icon)

        item.manifest.settingsActivityId?.let {
            holder.settings.visibility = View.VISIBLE
            holder.settings.setOnClickListener {
                onSettingsClicked?.invoke(item)
            }
        } ?: let {
            holder.settings.visibility = View.GONE
        }

    }
}