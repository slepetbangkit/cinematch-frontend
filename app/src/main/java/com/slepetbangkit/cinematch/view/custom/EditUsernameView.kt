package com.slepetbangkit.cinematch.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EditUsernameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    var onValidationChanged: ((Boolean) -> Unit)? = null

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val usernamePattern = "^[a-zA-Z0-9@./+\\-_]*$".toRegex()
                val isValid = usernamePattern.matches(s) && s.toString().isNotEmpty()
                onValidationChanged?.invoke(isValid)
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}
