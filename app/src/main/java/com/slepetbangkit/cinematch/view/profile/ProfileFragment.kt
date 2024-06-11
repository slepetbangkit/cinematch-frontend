package com.slepetbangkit.cinematch.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentProfileBinding
import com.slepetbangkit.cinematch.helpers.ProfileViewModelFactory
import com.slepetbangkit.cinematch.helpers.ViewModelFactory
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchUserViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var factory: ProfileViewModelFactory
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var username = arguments?.getString("username")
        if (username == null) {
            username = ""
        }

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        factory = ProfileViewModelFactory.getInstance(sessionRepository, username)
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.isOwnProfile.observe(viewLifecycleOwner) { isOwnProfile ->
            if (isOwnProfile) {
                binding.profileCard.setIsOwnProfileView(true)
            } else {
                binding.profileCard.setIsOwnProfileView(false)
            }
        }

        profileViewModel.profile.observe(viewLifecycleOwner) {
            it.followingCount?.let { followingCount -> binding.profileCard.setFollowingCount(followingCount) }
            it.followerCount?.let { followersCount -> binding.profileCard.setFollowersCount(followersCount) }
            it.username?.let { username -> binding.profileCard.setUsername(username) }
            it.bio?.let { bio -> binding.profileCard.setBio(bio) }
        }

        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.profileCard.visibility = View.GONE
                binding.shimmerViewContainer.let {
                    it.startShimmer()
                    it.visibility = View.VISIBLE
                }
            } else {
                binding.profileCard.visibility = View.VISIBLE
                binding.shimmerViewContainer.let {
                    it.stopShimmer()
                    it.visibility = View.GONE
                }
            }
        }

        binding.profileCard.setEdtProfileButtonClickListener {
            navController.navigate(R.id.action_navigation_profile_to_navigation_edit_profile)
        }

        binding.profileCard.setSettingsButtonClickListener {
            navController.navigate(R.id.action_navigation_profile_to_navigation_settings)
        }

        binding.profileCard.setFollowButtonClickListener {
            // TO-DO: Follow button click listener
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}