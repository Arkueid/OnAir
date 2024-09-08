package com.arkueid.onair.ui.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.arkueid.onair.R
import com.arkueid.onair.databinding.ItemSourceBinding
import com.google.android.material.radiobutton.MaterialRadioButton

/**
 * @author: Arkueid
 * @date: 2024/9/8
 * @desc:
 */
class SourceAdapter(
    context: Context,
    private val items: List<Map<String, Any?>>,
    private var selectedPosition: Int,
    private val onItemClickListener: ((Int) -> Unit)?
) :
    ArrayAdapter<Map<String, Any?>>(context, R.layout.item_source, items) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var view = convertView

        if (view == null) {
            val binding =
                ItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view = binding.root
            holder = ViewHolder(
                binding.sourceIcon,
                binding.sourceName,
                binding.radioBtn
            )
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        when (items[position]["icon"]) {
            is Int -> holder.icon.setImageResource(items[position]["icon"] as Int)
            is Drawable -> holder.icon.setImageDrawable(items[position]["icon"] as Drawable)
        }
        holder.optionText.text = items[position]["name"] as String
        holder.radioButton.setOnCheckedChangeListener(null)
        holder.radioButton.isChecked = position == selectedPosition

        view.setOnClickListener {
            select(position)
        }
        holder.radioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) select(position)
        }

        return view
    }

    private fun select(position: Int) {
        onItemClickListener?.invoke(position)
        if (selectedPosition != position) {
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(
        val icon: ImageView,
        val optionText: TextView,
        val radioButton: MaterialRadioButton
    )
}
