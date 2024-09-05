package com.arkueid.onair.ui.home

import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arkueid.onair.R
import com.arkueid.onair.databinding.ItemModuleBannerBinding
import com.arkueid.onair.databinding.ItemModuleNormalBinding
import com.arkueid.onair.domain.entity.Module
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator

/**
 * @author: Arkueid
 * @date: 2024/8/26
 * @desc:
 */
class ModuleAdapter(var data: List<Module>) : RecyclerView.Adapter<ModuleAdapter.ModuleVH>() {

    open class ModuleVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open val title: TextView
            get() {
                throw NotImplementedError("Not implemented")
            }
    }

    class NormalVH(binding: ItemModuleNormalBinding) : ModuleVH(binding.root) {
        override val title = binding.title
        val recyclerView = binding.recyclerView
    }

    class BannerVH(binding: ItemModuleBannerBinding) : ModuleVH(binding.root) {
        override val title = binding.title
        val bannerView = binding.bannerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleVH {
        when (viewType) {
            Module.BANNER -> {
                return BannerVH(
                    ItemModuleBannerBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            else -> {
                return NormalVH(
                    ItemModuleNormalBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ModuleVH, position: Int) {
        val module = data[position]
        bindTitle(holder, module)

        // bind items
        when (module.style) {
            Module.BANNER -> {
                val bannerVH = holder as BannerVH
                bannerVH.bannerView.let {
                    it.setAdapter(ModuleItemAdapterB(module.items))
                    it.setBannerGalleryEffect(0, 10)
                    it.indicator = CircleIndicator(holder.itemView.context)
                }
            }

            else -> {
                val normalVH = holder as NormalVH
                if (module.items.size >= 6 && (module.style in Module.SQUARE_GRID..Module.WIDE_RECTANGLE_GRID)) {
                    normalVH.recyclerView.run {
                        adapter = ModuleItemAdapterN(module.style, module.items.take(6))
                        layoutManager = GridLayoutManager(context, 3)
                        val paddingHorizontal = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            10f,
                            holder.itemView.resources.displayMetrics
                        ).toInt()
                        setPadding(paddingHorizontal, paddingTop, paddingHorizontal, paddingBottom)
                    }
                } else {
                    normalVH.recyclerView.run {
                        adapter = ModuleItemAdapterN(module.style, module.items)
                        layoutManager = LinearLayoutManager(
                            context, LinearLayoutManager.HORIZONTAL, false
                        )
                        val paddingHorizontal = 0
                        setPadding(paddingHorizontal, paddingTop, paddingHorizontal, paddingBottom)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].style
    }

    private fun bindTitle(holder: ModuleVH, module: Module) {
        if (TextUtils.isEmpty(module.title)) { // no title
            holder.title.visibility = View.GONE
        } else { // has title
            holder.title.visibility = View.VISIBLE // set visible
            if (TextUtils.isEmpty(module.moreUrl)) { // no more url
                holder.title.text = module.title
                holder.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            } else {
                // show more indicator
                holder.title.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.round_navigate_next_24, 0
                )
                // set clickable title which leads to more page
                holder.title.text = SpannableString(module.title).apply {
                    setSpan(object : ClickableSpan() {
                        override fun updateDrawState(ds: TextPaint) {
                            ds.color = Color.BLACK
                            ds.isUnderlineText = false
                        }

                        override fun onClick(widget: View) {
                            Intent(holder.itemView.context, MoreActivity::class.java).let {
                                it.putExtra("moreUrl", module.moreUrl)
                                holder.itemView.context.startActivity(it)
                            }
                        }

                    }, 0, module.title!!.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                }
                holder.title.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}