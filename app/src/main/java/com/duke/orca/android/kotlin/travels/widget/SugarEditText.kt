package com.duke.orca.android.kotlin.travels.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import com.duke.orca.android.kotlin.travels.R
import com.duke.orca.android.kotlin.travels.util.toPx
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class SugarEditText : TextInputLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        getAttrs(attrs, defStyleAttr)
    }

    private lateinit var textInputEditText: TextInputEditText

    private fun getAttrs(attrs: AttributeSet) {
        setTypedArray(context.obtainStyledAttributes(attrs, R.styleable.SugarEditText))
    }

    private fun getAttrs(attrs: AttributeSet, defStyleAttr: Int) {
        setTypedArray(
            context.obtainStyledAttributes(
                attrs,
                R.styleable.SugarEditText,
                defStyleAttr,
                0
            )
        )
    }

    private fun setTypedArray(typedArray: TypedArray) {
        addTextInputEditText(typedArray)
        typedArray.recycle()
    }

    private fun addTextInputEditText(typedArray: TypedArray) {
        val inputType = typedArray.getInt(R.styleable.SugarEditText_inputType, EditorInfo.TYPE_CLASS_TEXT)
        val textColor = typedArray.getColor(R.styleable.SugarEditText_textColor, colorOnBackground())
        val textInputEditTextHeight = typedArray.getDimensionPixelSize(R.styleable.SugarEditText_textInputEditTextHeight, LayoutParams.MATCH_PARENT)
        val textSize = typedArray.getDimension(R.styleable.SugarEditText_textSize, -1F)

        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            textInputEditTextHeight
        )

        layoutParams.setMargins(0, 0, 0, 1.toPx)

        textInputEditText = TextInputEditText(context).apply {
            this.layoutParams = layoutParams
            this.inputType = inputType
            setTextColor(textColor)
            this.textSize = textSize
            setPadding(8.toPx, 0, 8.toPx, 0)
        }

        addView(textInputEditText)
    }

    private fun colorOnBackground() = ContextCompat.getColor(context, R.color.material_on_background_emphasis_high_type)

    fun isBlank() = textInputEditText.text?.isBlank() ?: true
    fun isEmpty() = textInputEditText.text?.isEmpty() ?: true
    fun isNotBlank() = textInputEditText.text?.isNotBlank() ?: true
    fun isNotEmpty() = textInputEditText.text?.isNotEmpty() ?: true

    fun setError(error: String) {
        this.isErrorEnabled = true
        this.error = error
    }

    fun text() = textInputEditText.text.toString()
}