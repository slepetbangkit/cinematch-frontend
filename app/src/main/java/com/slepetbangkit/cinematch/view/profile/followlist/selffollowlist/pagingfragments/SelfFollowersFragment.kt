package com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist.pagingfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.FollowListItem
import com.slepetbangkit.cinematch.databinding.FragmentFollowersBinding
import com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist.SelfFollowListViewModel
import com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist.adapter.SelfFollowListItemAdapter

class SelfFollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding: FragmentFollowersBinding get() = _binding!!
    private lateinit var selfFollowListViewModel: SelfFollowListViewModel
    private lateinit var selfFollowListItemAdapter: SelfFollowListItemAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.navigation_self_profile)

        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        selfFollowListViewModel = ViewModelProvider(navBackStackEntry)[SelfFollowListViewModel::class.java]
        selfFollowListItemAdapter = SelfFollowListItemAdapter()
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.followersRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = selfFollowListItemAdapter
        }
        selfFollowListItemAdapter.setOnItemClickCallback(object : SelfFollowListItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FollowListItem) {
                data.username.let { username ->
                    val bundle = Bundle().apply {
                        putString("username", username)
                    }
                    navController.navigate(R.id.action_navigation_self_follow_list_to_navigation_other_profile, bundle)
                }
            }
        })

        selfFollowListViewModel.followList.observe(viewLifecycleOwner) {
            if (it.followers.isEmpty()) {
                binding.tvNoFollowers.visibility = View.VISIBLE
            } else {
                binding.tvNoFollowers.visibility = View.GONE
            }
            selfFollowListItemAdapter.submitList(it.followers)
        }

        selfFollowListViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
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
}