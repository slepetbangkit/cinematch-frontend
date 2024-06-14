package com.slepetbangkit.cinematch.view.profile.followlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentFollowListBinding
import com.slepetbangkit.cinematch.helpers.ProfileViewModelFactory
import com.slepetbangkit.cinematch.view.profile.followlist.adapter.FollowListAdapter

class FollowListFragment : Fragment() {
    private var _binding: FragmentFollowListBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var factory: ProfileViewModelFactory
    private lateinit var followListViewModel: FollowListViewModel
    var username: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        username = arguments?.getString("username")
        if (username == null) {
            username = ""
        }
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.navigation_profile)

        _binding = FragmentFollowListBinding.inflate(inflater, container, false)
        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        factory = ProfileViewModelFactory.getInstance(sessionRepository, username.toString())
        followListViewModel = ViewModelProvider(navBackStackEntry, factory)[FollowListViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        context?.let {
            val adapter = FollowListAdapter(it as FragmentActivity)
            viewPager.adapter = adapter
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Following"
                1 -> tab.text = "Followers"
            }
        }.attach()

        val tabIndex = arguments?.getInt("tabIndex", 0)
        viewPager.setCurrentItem(tabIndex ?: 0, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}