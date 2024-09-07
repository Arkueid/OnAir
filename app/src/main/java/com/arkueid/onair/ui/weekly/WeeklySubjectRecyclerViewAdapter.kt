package com.arkueid.onair.ui.weekly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arkueid.onair.data.entity.play
import com.arkueid.onair.databinding.ItemWeeklySubjectBinding
import com.arkueid.plugin.data.entity.WeeklyAnime
import com.bumptech.glide.Glide


class WeeklySubjectRecyclerViewAdapter(
    var data: List<WeeklyAnime>
) : RecyclerView.Adapter<WeeklySubjectRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemWeeklySubjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.title.text = item.anime.name
        Glide.with(holder.cover)
            .asBitmap()
            .load(item.anime.cover)
            .into(holder.cover)
        holder.overlay.setOnClickListener { item.anime.play(holder.itemView.context) }
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(binding: ItemWeeklySubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val cover = binding.cover
        val overlay = binding.overlay
    }

}