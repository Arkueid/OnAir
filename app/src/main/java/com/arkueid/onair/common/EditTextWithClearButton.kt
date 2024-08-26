package com.arkueid.onair.common

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatEditText
import com.arkueid.onair.R

class EditTextWithClearButton(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
    AppCompatEditText(context, attributeSet, defStyle), TextWatcher, OnTouchListener,
    OnFocusChangeListener {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null)

    init {
        // make sure keyboard can be shown on user click
        isFocusableInTouchMode = true
        isSingleLine = true
        setOnTouchListener(this)
        addTextChangedListener(this)
        onFocusChangeListener = this
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null
            && event!!.action == MotionEvent.ACTION_UP
            && event.x >= width - paddingRight - compoundDrawables[2].bounds.width()
        ) {
            text?.clear()
            performClick()
            return true
        }
        return false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (text?.length == 0) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            if (hasFocus()) {
                setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_cancel_24, 0)
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {}
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            if (text!!.isNotEmpty()) {
                setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_cancel_24, 0)
            }
        } else {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_cancel_24, 0)
        }
    }

}