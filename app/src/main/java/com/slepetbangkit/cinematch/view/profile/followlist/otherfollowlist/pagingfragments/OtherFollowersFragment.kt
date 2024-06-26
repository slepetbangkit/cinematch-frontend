package com.slepetbangkit.cinematch.view.profile.followlist.otherfollowlist.pagingfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.FollowListItem
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.databinding.FragmentFollowersBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.OtherProfileViewModelFactory
import com.slepetbangkit.cinematch.view.profile.followlist.otherfollowlist.OtherFollowListViewModel
import com.slepetbangkit.cinematch.view.profile.followlist.otherfollowlist.adapter.OtherFollowListItemAdapter
import kotlinx.coroutines.launch

class OtherFollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding: FragmentFollowersBinding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var factory: OtherProfileViewModelFactory
    private lateinit var otherFollowListViewModel: OtherFollowListViewModel
    private lateinit var otherFollowListItemAdapter: OtherFollowListItemAdapter
    private lateinit var navController: NavController
    private var sessionUsername = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val username = arguments?.getString("username") ?: ""

        val navBackStackEntry = findNavController().getBackStackEntry(R.id.navigation_other_follow_list)

        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        userRepository = Injection.provideUserRepository(requireContext())

        factory = OtherProfileViewModelFactory.getInstance(sessionRepository, userRepository)
        factory.updateUsername(username)

        otherFollowListViewModel = ViewModelProvider(navBackStackEntry)[OtherFollowListViewModel::class.java]
        otherFollowListItemAdapter = OtherFollowListItemAdapter()
        navController = findNavController()

        lifecycleScope.launch {
            sessionUsername = sessionRepository.getUsername()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.followersRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = otherFollowListItemAdapter
        }
        otherFollowListItemAdapter.setOnItemClickCallback(object : OtherFollowListItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FollowListItem) {
                data.username.let { username ->
                    if (username == sessionUsername) {
                        navController.navigate(R.id.action_navigation_other_follow_list_to_navigation_self_profile)
                        return
                    } else {
                        val bundle = Bundle().apply {
                            putString("username", username)
                        }
                        navController.navigate(R.id.action_navigation_other_follow_list_to_navigation_other_profile, bundle)
                    }
                }
            }
        })

        otherFollowListViewModel.followList.observe(viewLifecycleOwner) {
            if (it.followers.isEmpty()) {
                binding.tvNoFollowers.visibility = View.VISIBLE
            } else {
                binding.tvNoFollowers.visibility = View.GONE
            }
            otherFollowListItemAdapter.submitList(it.followers)
        }

        otherFollowListViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.shimmerViewContainer.let {
                if (isLoading) {
                    it.startShimmer()
                    it.visibility = View.VISIBLE
                } else {
                    it.stopShimmer()
                    it.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
