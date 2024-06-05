package com.slepetbangkit.cinematch.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.slepetbangkit.cinematch.data.local.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.databinding.FragmentProfileBinding
import com.slepetbangkit.cinematch.helpers.ViewModelFactory

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionPrefs: SessionPreferences
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        sessionPrefs = SessionPreferences.getInstance(requireContext().dataStore)
        profileViewModel = ViewModelFactory.getInstance(sessionPrefs).create(ProfileViewModel::class.java)

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
            // TO-DO: Implement edit profile feature
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}