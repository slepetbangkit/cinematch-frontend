package com.slepetbangkit.cinematch.view.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.slepetbangkit.cinematch.view.search.pagerfragments.MovieSearchFragment
import com.slepetbangkit.cinematch.view.search.pagerfragments.UserSearchFragment


class SearchViewAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MovieSearchFragment()
            1 -> UserSearchFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
