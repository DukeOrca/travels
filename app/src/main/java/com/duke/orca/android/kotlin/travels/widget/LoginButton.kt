package com.duke.orca.android.kotlin.travels.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.duke.orca.android.kotlin.travels.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class LoginButton: MaterialCardView {
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

    private fun getAttrs(attrs: AttributeSet) {
        setTypedArray(context.obtainStyledAttributes(attrs, R.styleable.LoginButton))
    }

    private fun getAttrs(attrs: AttributeSet, defStyleAttr: Int) {
        setTypedArray(
            context.obtainStyledAttributes(
                attrs,
                R.styleable.LoginButton,
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
        //val backgroundColor = typedArray.getColor(R.styleable.LoginButton_backgroundColor, Color.WHITE)
        val symbol = typedArray.getResourceId(R.styleable.LoginButton_symbol, 0)
        val text = typedArray.getText(R.styleable.LoginButton_text)
        val textColor = typedArray.getColor(R.styleable.LoginButton_textColor2, Color.WHITE)

        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.login_button, this, false)
        addView(view)

        findViewById<ImageView>(R.id.image_view).setImageResource(symbol)
        findViewById<TextView>(R.id.text_view).apply {
            this.text = text
            this.setTextColor(textColor)
        }
    }
}