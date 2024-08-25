package com.arkueid.onair.presentation.weekly.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arkueid.onair.databinding.ItemWeeklySubjectBinding
import com.arkueid.onair.presentation.weekly.model.WeeklySubjectHolder
import com.bumptech.glide.Glide


class WeeklySubjectRecyclerViewAdapter(
    var data: List<WeeklySubjectHolder>
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
        holder.title.text = item.title
        Glide.with(holder.cover)
            .asBitmap()
            .load(item.cover)
            .into(holder.cover)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(binding: ItemWeeklySubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val cover = binding.cover
    }

}