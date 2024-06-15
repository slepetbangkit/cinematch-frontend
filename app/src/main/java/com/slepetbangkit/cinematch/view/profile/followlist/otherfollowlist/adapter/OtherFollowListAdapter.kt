package com.slepetbangkit.cinematch.view.profile.followlist.otherfollowlist.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.slepetbangkit.cinematch.view.profile.followlist.otherfollowlist.pagingfragments.OtherFollowersFragment
import com.slepetbangkit.cinematch.view.profile.followlist.otherfollowlist.pagingfragments.OtherFollowingFragment
import com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist.pagingfragments.SelfFollowersFragment
import com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist.pagingfragments.SelfFollowingFragment

class OtherFollowListAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OtherFollowingFragment()
            1 -> OtherFollowersFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}