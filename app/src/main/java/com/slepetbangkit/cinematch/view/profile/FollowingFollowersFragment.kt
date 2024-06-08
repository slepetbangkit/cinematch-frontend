package com.slepetbangkit.cinematch.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.databinding.FragmentFollowingFollowersBinding

class FollowingFollowersFragment : Fragment() {
    private var _binding: FragmentFollowingFollowersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following_followers, container, false)
    }
}