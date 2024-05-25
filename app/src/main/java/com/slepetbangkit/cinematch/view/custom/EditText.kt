package com.slepetbangkit.cinematch.view.custom

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.slepetbangkit.cinematch.R

class EditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    var onValidationChanged: ((Boolean) -> Unit)? = null

    private var fieldType: Int = 0

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EditText,
            0, 0
        ).apply {
            try {
                fieldType = getInt(R.styleable.EditText_fieldType, 0)
            } finally {
                recycle()
            }
        }

        inputType = when (fieldType) {
            0, 1 -> InputType.TYPE_CLASS_TEXT
            2 -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            else -> InputType.TYPE_CLASS_TEXT
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val isValid = when (fieldType) {
                    0 -> validateUsername(s)
                    1 -> validateEmail(s)
                    2 -> validatePassword(s)
                    else -> false
                }
                onValidationChanged?.invoke(isValid)
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    private fun validateUsername(s: CharSequence): Boolean {
        val usernamePattern = "^[a-zA-Z0-9@./+\\-_]*$".toRegex()
        return usernamePattern.matches(s) && s.toString().isNotEmpty()
    }

    private fun validateEmail(s: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()
    }

    private fun validatePassword(s: CharSequence): Boolean {
        return s.toString().length >= 8
    }
}