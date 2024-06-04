package com.slepetbangkit.cinematch.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.slepetbangkit.cinematch.R

class ActivityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

// To use custom font in SpannableString for Activity Text
// val username = "username"
// val action = " started following you"
// val fullText = "$username$action"
//
// val spannableString = SpannableString(fullText)
//
// val boldTypeface = ResourcesCompat.getFont(context, R.font.plus_jakarta_sans_bold) // replace with your bold font resource ID
// val boldSpan = CustomTypefaceSpan(boldTypeface!!)
// spannableString.setSpan(boldSpan, 0, username.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
//
// val textView = findViewById<TextView>(R.id.tv_activity)
// textView.text = spannableString