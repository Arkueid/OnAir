package com.arkueid.onair.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.arkueid.onair.R
import com.arkueid.onair.databinding.ModuleItemNormalBinding
import com.arkueid.onair.ui.home.model.Module
import com.arkueid.onair.ui.home.model.ModuleItem
import com.bumptech.glide.Glide

/**
 * @author: Arkueid
 * @date: 2024/8/26
 * @desc: adapter for normal module which using `RecyclerView` to display
 */
class ModuleItemAdapterN(private val style: Int, var data: List<ModuleItem>) :
    RecyclerView.Adapter<ModuleItemAdapterN.ItemVH>() {

    class ItemVH(binding: ModuleItemNormalBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val cover = binding.cover
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemVH(
            ModuleItemNormalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = data[position]
        holder.title.text = item.title
        Glide.with(holder.itemView).asBitmap().load(item.cover).into(holder.cover)

        when (style) {
            Module.SQUARE_GRID, Module.SQUARE_LIST -> {
                holder.cover.run {
                    layoutParams = layoutParams.apply {
                        width = resources.getDimension(R.dimen.wideModuleItemHeight).toInt()
                        height = resources.getDimension(R.dimen.wideModuleItemHeight).toInt()
                    }
                }
            }

            Module.TALL_RECTANGLE_GRID, Module.TALL_RECTANGLE_LIST -> {
                holder.cover.run {
                    layoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply {
                        width = if (style == Module.TALL_RECTANGLE_GRID) {
                            LayoutParams.MATCH_PARENT
                        } else {
                            resources.getDimension(R.dimen.tallModuleItemWidth).toInt()
                        }
                        if (style == Module.TALL_RECTANGLE_GRID) {
                            dimensionRatio = "H,3:4"
                            height = 0
                        } else {
                            dimensionRatio = ""
                            height = resources.getDimension(R.dimen.tallModuleItemHeight).toInt()
                        }
                    }
                }
            }

            Module.WIDE_RECTANGLE_GRID, Module.WIDE_RECTANGLE_LIST -> {
                holder.cover.run {
                    layoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply {
                        width = if (style == Module.WIDE_RECTANGLE_GRID) {
                            LayoutParams.MATCH_PARENT
                        } else {
                            resources.getDimension(R.dimen.wideModuleItemWidth).toInt()
                        }
                        if (style == Module.WIDE_RECTANGLE_GRID) {
                            dimensionRatio = "H,4:3"
                            height = 0
                        } else {
                            dimensionRatio = ""
                            height = resources.getDimension(R.dimen.wideModuleItemHeight).toInt()
                        }
                    }
                }
            }
        }
    }
}