package com.slepetbangkit.cinematch.view.profile.otherprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.databinding.FragmentOtherProfileBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.OtherProfileViewModelFactory
import com.slepetbangkit.cinematch.factories.SelfProfileViewModelFactory
import com.slepetbangkit.cinematch.view.profile.selfprofile.SelfProfileViewModel
import kotlinx.coroutines.launch

class OtherProfileFragment : Fragment() {
    private var _binding: FragmentOtherProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var factory: OtherProfileViewModelFactory
    private lateinit var selfFactory: SelfProfileViewModelFactory
    private lateinit var otherProfileViewModel: OtherProfileViewModel
    private lateinit var selfProfileViewModel: SelfProfileViewModel
    private lateinit var navController: NavController
    private var username: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        username = arguments?.getString("username") ?: ""

        _binding = FragmentOtherProfileBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        userRepository = Injection.provideUserRepository(requireContext())

        factory = OtherProfileViewModelFactory.getInstance(sessionRepository, userRepository)
        factory.updateUsername(username)

        selfFactory = SelfProfileViewModelFactory.getInstance(sessionRepository, userRepository)
        otherProfileViewModel = ViewModelProvider(this, factory)[OtherProfileViewModel::class.java]
        selfProfileViewModel = ViewModelProvider(requireActivity(), selfFactory)[SelfProfileViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        otherProfileViewModel.profile.observe(viewLifecycleOwner) {
            it.followingCount.let { followingCount -> binding.profileCard.setFollowingCount(followingCount) }
            it.followerCount.let { followersCount -> binding.profileCard.setFollowersCount(followersCount) }
            it.username.let { username -> binding.profileCard.setUsername(username) }
            it.bio.let { bio -> binding.profileCard.setBio(bio) }
            it.isFollowed.let { isFollowed -> binding.profileCard.setIsFollowed(isFollowed) }
            it.isFollowingUser.let { isFollowingUser -> binding.profileCard.setIsFollowingUser(isFollowingUser) }
        }

        otherProfileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.profileCard.visibility = View.GONE
                binding.listsTv.visibility = View.GONE
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

        binding.profileCard.setFollowingLayoutClickListener {
            val bundle = Bundle().apply {
                putString("username", username)
                putInt("tabIndex", 0)
            }
            navController.navigate(R.id.action_navigation_other_profile_to_navigation_other_follow_list, bundle)
        }

        binding.profileCard.setFollowersLayoutClickListener {
            val bundle = Bundle().apply {
                putString("username", username)
                putInt("tabIndex", 1)
            }
            navController.navigate(R.id.action_navigation_other_profile_to_navigation_other_follow_list, bundle)
        }

        binding.profileCard.setFollowButtonClickListener {
            lifecycleScope.launch {
                otherProfileViewModel.follow()
                otherProfileViewModel.setIsFollowed(true)
                selfProfileViewModel.incrementFollowingCount()
            }
        }

        binding.profileCard.setUnfollowButtonClickListener {
            lifecycleScope.launch {
                otherProfileViewModel.unfollow()
                otherProfileViewModel.setIsFollowed(false)
                selfProfileViewModel.decrementFollowingCount()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
