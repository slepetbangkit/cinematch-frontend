package com.slepetbangkit.cinematch.view.profile.otherprofile

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.PlaylistsItem
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.databinding.FragmentOtherProfileBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.MovieListViewModelFactory
import com.slepetbangkit.cinematch.factories.OtherProfileViewModelFactory
import com.slepetbangkit.cinematch.factories.SelfProfileViewModelFactory
import com.slepetbangkit.cinematch.view.profile.movielist.ProfileMovieListAdapter
import com.slepetbangkit.cinematch.view.profile.selfprofile.SelfProfileViewModel
import kotlinx.coroutines.launch

class OtherProfileFragment : Fragment() {
    private var _binding: FragmentOtherProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var movieListRepository: MovieListRepository
    private lateinit var factory: OtherProfileViewModelFactory
    private lateinit var selfFactory: SelfProfileViewModelFactory
    private lateinit var movieListFactory: MovieListViewModelFactory
    private lateinit var otherProfileViewModel: OtherProfileViewModel
    private lateinit var selfProfileViewModel: SelfProfileViewModel
    private lateinit var navController: NavController
    private lateinit var movieListAdapter: ProfileMovieListAdapter
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        username = arguments?.getString("username") ?: ""

        _binding = FragmentOtherProfileBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())

        userRepository = Injection.provideUserRepository(requireContext())
        movieListRepository = Injection.provideMovieListRepository(requireContext())

        factory = OtherProfileViewModelFactory.getInstance(sessionRepository, userRepository)
        factory.updateUsername(username)

        selfFactory = SelfProfileViewModelFactory.getInstance(sessionRepository, userRepository)
        otherProfileViewModel = ViewModelProvider(this, factory)[OtherProfileViewModel::class.java]
        selfProfileViewModel = ViewModelProvider(requireActivity(), selfFactory)[SelfProfileViewModel::class.java]
        movieListFactory = MovieListViewModelFactory.getInstance(sessionRepository, movieListRepository)
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

        otherProfileViewModel.profile.observe(viewLifecycleOwner) {
            it.profilePicture.let { profilePicture -> binding.profileCard.setProfileImage(profilePicture.toString()) }
            it.followingCount.let { followingCount -> binding.profileCard.setFollowingCount(followingCount) }
            it.followerCount.let { followersCount -> binding.profileCard.setFollowersCount(followersCount) }
            it.username.let { username -> binding.profileCard.setUsername(username) }
            it.bio.let { bio -> binding.profileCard.setBio(bio) }
            it.isFollowed.let { isFollowed -> binding.profileCard.setIsFollowed(isFollowed) }
            it.isFollowingUser.let { isFollowingUser -> binding.profileCard.setIsFollowingUser(isFollowingUser) }
            it.playlists.let { playlists -> movieListAdapter.submitList(playlists) }

            it.isBlended.let { isBlended ->
                if (isBlended) {
                    binding.btnCreateBlend.visibility = View.INVISIBLE
                    binding.btnBlendedList.visibility = View.VISIBLE
                    binding.btnBlendedList.isClickable = false
                } else {
                    binding.btnCreateBlend.visibility = View.VISIBLE
                    binding.btnBlendedList.visibility = View.INVISIBLE
                }
            }
        }

        otherProfileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.profileCard.visibility = View.GONE
                binding.listsTv.visibility = View.GONE
                binding.btnCreateBlend.visibility = View.GONE
                binding.btnBlendedList.visibility = View.GONE
                binding.movieListRv.visibility = View.GONE
                binding.shimmerViewContainer.let {
                    it.startShimmer()
                    it.visibility = View.VISIBLE
                }
            } else {
                binding.profileCard.visibility = View.VISIBLE
                binding.listsTv.visibility = View.VISIBLE
                binding.btnCreateBlend.visibility = View.VISIBLE
                binding.movieListRv.visibility = View.VISIBLE
                binding.shimmerViewContainer.let {
                    it.stopShimmer()
                    it.visibility = View.GONE
                }
            }
        }

        otherProfileViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showErrorDialog(it)
                otherProfileViewModel.clearError()
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

        binding.btnCreateBlend.setOnClickListener {
            lifecycleScope.launch {
                otherProfileViewModel.blendList()
                showBlendListConfirmationDialog { fetchOtherProfile() }
            }
        }

        movieListAdapter.setOnItemClickCallback(object : ProfileMovieListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlaylistsItem) {
                data.id.let { listId ->
                    val bundle = Bundle().apply {
                        putString("listId", listId)
                    }
                    Log.d("OtherProfileFragment", "onItemClicked: $listId")
                    navController.navigate(R.id.action_navigation_other_profile_to_movieListFragment, bundle)
                }
            }
        })
    }

    private fun showBlendListConfirmationDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle("Create a Blend Movie List")
            .setMessage("Would you like to create a new blend movie list with $username?")
            .setPositiveButton("Create") { _, _ -> onConfirm() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        fetchOtherProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchOtherProfile() {
        lifecycleScope.launch {
            otherProfileViewModel.getOtherProfile()
        }
    }
}
