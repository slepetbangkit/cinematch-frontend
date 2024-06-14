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

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        factory = ProfileViewModelFactory.getInstance(sessionRepository, username.toString())
        profileViewModel = ViewModelProvider(navBackStackEntry, factory)[ProfileViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.isProfileUpdated.observe(viewLifecycleOwner) { isUpdated ->
            if (isUpdated) {
                profileViewModel.fetchProfile()
                profileViewModel.setProfileUpdated(false)
            }
        }

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
            it.username?.let { uname ->
                binding.profileCard.setUsername(uname)
                username = uname
            }
            it.bio?.let { bio -> binding.profileCard.setBio(bio) }
//            it.isFollowed?.let { isFollowed -> if (isFollowed) binding.profileCard.setIsFollowed(true) else binding.profileCard.setIsFollowed(false) }
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

        binding.profileCard.setFollowCountClickListener {
            val bundle = Bundle().apply {
                putString("username", username)
            }
            navController.navigate(R.id.action_navigation_profile_to_navigation_follow_list, bundle)
        }

        binding.profileCard.setEdtProfileButtonClickListener {
            val bundle = Bundle().apply {
                putString("username", username)
            }
            navController.navigate(R.id.action_navigation_profile_to_navigation_edit_profile, bundle)
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