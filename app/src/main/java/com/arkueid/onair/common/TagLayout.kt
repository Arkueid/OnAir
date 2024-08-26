package com.arkueid.onair.common

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import com.arkueid.onair.R

class TagLayout(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
    ViewGroup(context, attributeSet, defStyle) {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null)

    // vertical gap, default 10dp
    private var vGap =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)

    // horizontal gap, default 10dp
    private var hGap =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)

    init {
        attributeSet?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TagLayout)
            vGap = typedArray.getDimension(R.styleable.TagLayout_tagVGap, vGap)
            hGap = typedArray.getDimension(R.styleable.TagLayout_tagHGap, hGap)
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val availableWidth = MeasureSpec.getSize(widthMeasureSpec)
        val availableHeight = MeasureSpec.getSize(heightMeasureSpec)

        var startX = paddingLeft
        var startY = paddingTop

        var widthSpec = startX + paddingRight
        var heightSpec = startY + paddingBottom

        var maxChildHeight = 0

        // layout children from the last to first
        val lastIndex = childCount - 1
        for (i in lastIndex downTo 0) {
            val child = getChildAt(i)
            if (child.visibility == GONE) {
                continue
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            // place child to next line
            if (startX + childWidth + paddingRight > availableWidth) {
                startX = paddingLeft
                startY += childHeight + vGap.toInt()
                heightSpec += maxChildHeight + vGap.toInt()
            } else {
                widthSpec = maxOf(widthSpec, startX + childWidth + paddingRight)
                maxChildHeight = maxOf(maxChildHeight, childHeight)
            }
            startX += childWidth + hGap.toInt()
            if (startY + childHeight + paddingBottom > availableHeight) {
                break
            }
        }

        heightSpec += maxChildHeight

        setMeasuredDimension(
            if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) { // match_parent | give_width
                availableWidth
            } else { // wrap_content
                widthSpec
            },
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) { // match_parent | give_height
                availableHeight
            } else { // wrap_content
                heightSpec
            }
        )

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val availableWidth = r - l
        val availableHeight = b - t

        var startX = paddingLeft
        var startY = paddingTop

        val lastIndex = childCount - 1
        for (i in lastIndex downTo  0) {
            val child = getChildAt(i)
            if (child.visibility == GONE) {
                continue
            }
            // place child to next line
            if (startX + child.measuredWidth + paddingRight > availableWidth) {
                startX = l + paddingLeft
                startY += child.measuredHeight + vGap.toInt()
            }
            child.layout(
                startX,
                startY,
                startX + child.measuredWidth,
                startY + child.measuredHeight
            )
            startX += child.measuredWidth + hGap.toInt()
            if (startY + child.measuredHeight + paddingBottom > availableHeight) { // no more space
                break
            }
        }
    }
}