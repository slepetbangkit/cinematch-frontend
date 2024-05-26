package com.slepetbangkit.cinematch.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class EditEmailView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    var onValidationChanged: ((Boolean) -> Unit)? = null

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isValid = isValidEmail(s) && s.toString().isNotEmpty()
                onValidationChanged?.invoke(isValid)
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing.
            }
        })
    }

    private fun isValidEmail(email: CharSequence?): Boolean {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
