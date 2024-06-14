package com.slepetbangkit.cinematch.view.profile.followlist.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.slepetbangkit.cinematch.view.profile.followlist.pagingfragments.FollowersFragment
import com.slepetbangkit.cinematch.view.profile.followlist.pagingfragments.FollowingFragment

class FollowListAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowingFragment()
            1 -> FollowersFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}