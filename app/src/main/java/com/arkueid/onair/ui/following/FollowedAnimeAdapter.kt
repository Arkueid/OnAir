package com.arkueid.onair.ui.following

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arkueid.onair.data.entity.play
import com.arkueid.onair.databinding.ItemFollowedAnimeBinding
import com.arkueid.plugin.data.entity.Anime
import com.bumptech.glide.Glide

/**
 * @author: Arkueid
 * @date: 2024/9/3
 * @desc:
 */
class FollowedAnimeAdapter(
    private val onLikeClicked: ((Anime) -> Unit)? = null
) : ListAdapter<Anime, FollowedAnimeAdapter.FollowingAnimeViewHolder>(object :
    DiffUtil.ItemCallback<Anime>() {
    override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem.id == newItem.id && oldItem.sourceId == newItem.sourceId
    }

    override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem.cover == newItem.cover && oldItem.url == newItem.url
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingAnimeViewHolder {
        return FollowingAnimeViewHolder(
            ItemFollowedAnimeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FollowingAnimeViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.name
        Glide.with(holder.cover).asBitmap().load(item.cover).into(holder.cover)
        holder.likeBtn.isSelected = true
        holder.likeBtn.setOnClickListener {
            holder.likeBtn.run {
                isSelected = !isSelected
                onLikeClicked?.invoke(item)
            }
        }
        holder.sourceName.text = item.sourceName
        holder.clickable.setOnClickListener { item.play(holder.itemView.context) }
    }

    class FollowingAnimeViewHolder(binding: ItemFollowedAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val cover = binding.cover
        val likeBtn = binding.likeBtn
        val sourceName = binding.sourceName
        val clickable = binding.clickable
    }
}



