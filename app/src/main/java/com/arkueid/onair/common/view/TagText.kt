package com.arkueid.onair.common.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setPadding
import com.arkueid.onair.R

class TagText(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
    AppCompatTextView(context, attributeSet, defStyle) {
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null)

    init {
        setTextColor(context.getColor(R.color.black))
        textSize = 14f
        setPadding(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                5f,
                resources.displayMetrics
            ).toInt()
        )
        background = AppCompatResources.getDrawable(context, R.drawable.tag_background)
    }
}