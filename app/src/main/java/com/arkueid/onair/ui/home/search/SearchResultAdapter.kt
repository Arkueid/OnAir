package com.arkueid.onair.ui.home.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arkueid.onair.R
import com.arkueid.onair.data.entity.play
import com.arkueid.onair.databinding.ItemSearchResultBinding
import com.arkueid.plugin.data.entity.SearchResult
import com.bumptech.glide.Glide

/**
 * @author: Arkueid
 * @date: 2024/8/29
 * @desc:
 */
class SearchResultAdapter :
    RecyclerView.Adapter<SearchResultAdapter.VH>() {

    private var data: List<SearchResult> = emptyList()

    class VH(binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val cover = binding.cover
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<SearchResult>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = data[position]
        holder.title.text = item.anime.name
        Glide.with(holder.itemView)
            .asBitmap()
            .placeholder(R.drawable.placeholder)
            .load(item.anime.cover)
            .into(holder.cover)
        holder.itemView.setOnClickListener { item.anime.play(holder.itemView.context) }
    }
}