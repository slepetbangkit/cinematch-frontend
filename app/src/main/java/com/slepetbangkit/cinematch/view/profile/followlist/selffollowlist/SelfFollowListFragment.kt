package com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist

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
import com.slepetbangkit.cinematch.databinding.FragmentSelfFollowListBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.SelfProfileViewModelFactory
import com.slepetbangkit.cinematch.view.profile.selfprofile.SelfProfileViewModel
import com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist.adapter.SelfFollowListAdapter
import kotlinx.coroutines.launch

class SelfFollowListFragment : Fragment() {
    private var _binding: FragmentSelfFollowListBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var factory: SelfProfileViewModelFactory
    private lateinit var selfFollowListViewModel: SelfFollowListViewModel
    private lateinit var selfProfileViewModel: SelfProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.navigation_self_profile)

        _binding = FragmentSelfFollowListBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        userRepository = Injection.provideUserRepository(requireContext())
        factory = SelfProfileViewModelFactory.getInstance(sessionRepository, userRepository)
        selfFollowListViewModel = ViewModelProvider(navBackStackEntry, factory)[SelfFollowListViewModel::class.java]
        selfProfileViewModel = ViewModelProvider(requireActivity())[SelfProfileViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launch {
            binding.usernameTv.text = sessionRepository.getUsername()
        }

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        context?.let {
            val adapter = SelfFollowListAdapter(it as FragmentActivity)
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

        selfProfileViewModel.profile.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                selfFollowListViewModel.getSelfFollowList()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}