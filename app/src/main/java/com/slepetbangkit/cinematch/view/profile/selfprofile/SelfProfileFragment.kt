package com.slepetbangkit.cinematch.view.profile.selfprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.databinding.FragmentSelfProfileBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.SelfProfileViewModelFactory
import kotlinx.coroutines.launch

class SelfProfileFragment : Fragment() {
    private var _binding: FragmentSelfProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var factory: SelfProfileViewModelFactory
    private lateinit var selfProfileViewModel: SelfProfileViewModel
    private lateinit var navController: NavController
    private lateinit var username: String
    private lateinit var movieListAdapter: ProfileMovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelfProfileBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        userRepository = Injection.provideUserRepository(requireContext())
        factory = SelfProfileViewModelFactory.getInstance(sessionRepository, userRepository)
        selfProfileViewModel = ViewModelProvider(requireActivity(), factory)[SelfProfileViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieListAdapter = ProfileMovieListAdapter()
        binding.movieListRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieListAdapter
        }

        selfProfileViewModel.profile.observe(viewLifecycleOwner) {
            it.followingCount.let { followingCount -> binding.profileCard.setFollowingCount(followingCount) }
            it.followerCount.let { followersCount -> binding.profileCard.setFollowersCount(followersCount) }
            it.username.let { uname ->
                binding.profileCard.setUsername(uname)
                username = uname
            }
            it.bio.let { bio -> binding.profileCard.setBio(bio) }
            it.playlists.let { playlists -> movieListAdapter.submitList(playlists) }
        }

        selfProfileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.profileCard.visibility = View.GONE
                binding.shimmerViewContainer.let {
                    it.startShimmer()
                    it.visibility = View.VISIBLE
                }
                binding.movieListRv.visibility = View.GONE
                binding.btnAddMovielist.visibility = View.GONE
                binding.listsTv.visibility = View.GONE
            } else {
                binding.profileCard.visibility = View.VISIBLE
                binding.shimmerViewContainer.let {
                    it.stopShimmer()
                    it.visibility = View.GONE
                }
                binding.movieListRv.visibility = View.VISIBLE
                binding.btnAddMovielist.visibility = View.VISIBLE
                binding.listsTv.visibility = View.VISIBLE
            }
        }

        binding.profileCard.setFollowingLayoutClickListener {
            val bundle = Bundle().apply {
                putInt("tabIndex", 0)
            }
            navController.navigate(R.id.action_navigation_self_profile_to_navigation_self_follow_list, bundle)
        }

        binding.profileCard.setFollowersLayoutClickListener {
            val bundle = Bundle().apply {
                putInt("tabIndex", 1)
            }
            navController.navigate(R.id.action_navigation_self_profile_to_navigation_self_follow_list, bundle)
        }

        binding.profileCard.setEdtProfileButtonClickListener {
            navController.navigate(R.id.action_navigation_self_profile_to_navigation_edit_profile)
        }

        binding.profileCard.setSettingsButtonClickListener {
            navController.navigate(R.id.action_navigation_self_profile_to_navigation_settings)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchProfileData()
    }

    private fun fetchProfileData() {
        selfProfileViewModel.viewModelScope.launch {
            selfProfileViewModel.getSelfProfile()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
