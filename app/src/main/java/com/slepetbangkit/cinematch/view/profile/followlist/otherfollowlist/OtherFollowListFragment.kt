package com.slepetbangkit.cinematch.view.profile.followlist.otherfollowlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.databinding.FragmentOtherFollowListBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.OtherProfileViewModelFactory
import com.slepetbangkit.cinematch.view.profile.followlist.otherfollowlist.adapter.OtherFollowListAdapter
import com.slepetbangkit.cinematch.view.profile.otherprofile.OtherProfileViewModel
import kotlinx.coroutines.launch

class OtherFollowListFragment : Fragment() {
    private var _binding: FragmentOtherFollowListBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var factory: OtherProfileViewModelFactory
    private lateinit var otherFollowListViewModel: OtherFollowListViewModel
    private var username: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        username = arguments?.getString("username") ?: ""

        val navBackStackEntry = findNavController().getBackStackEntry(R.id.navigation_other_follow_list)

        _binding = FragmentOtherFollowListBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        userRepository = Injection.provideUserRepository(requireContext())

        factory = OtherProfileViewModelFactory.getInstance(sessionRepository, userRepository)
        factory.updateUsername(username)

        otherFollowListViewModel = ViewModelProvider(navBackStackEntry, factory)[OtherFollowListViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.usernameTv.text = username

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        context?.let {
            val adapter = OtherFollowListAdapter(it as FragmentActivity)
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
