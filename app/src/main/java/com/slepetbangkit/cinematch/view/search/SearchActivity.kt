package com.slepetbangkit.cinematch.view.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.slepetbangkit.cinematch.R

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .commitNow()
        }
    }
}
