package com.arkueid.onair.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arkueid.onair.R
import com.arkueid.onair.databinding.ModuleItemBannerBinding
import com.arkueid.plugin.data.entity.Anime
import com.bumptech.glide.Glide
import com.youth.banner.adapter.BannerAdapter

/**
 * @author: Arkueid
 * @date: 2024/8/26
 * @desc: adapter for module banner which uses banner adapter to display
 */
class ModuleItemAdapterB(data: List<Anime>) :
    BannerAdapter<Anime, ModuleItemAdapterB.BannerVH>(data) {

    class BannerVH(binding: ModuleItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        val cover = binding.cover
        val title = binding.title
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerVH {
        return BannerVH(
            ModuleItemBannerBinding.inflate(
                LayoutInflater.from(parent!!.context), parent, false
            )
        )
    }

    override fun onBindView(holder: BannerVH?, data: Anime?, position: Int, size: Int) {
        if (data == null || holder == null) return

        holder.title.text = data.name
        Glide.with(holder.itemView)
            .asBitmap()
            .placeholder(R.drawable.placeholder)
            .load(data.cover)
            .into(holder.cover)
    }

}