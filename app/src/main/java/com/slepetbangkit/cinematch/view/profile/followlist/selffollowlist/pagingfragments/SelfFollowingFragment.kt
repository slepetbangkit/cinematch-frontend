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
import com.slepetbangkit.cinematch.databinding.FragmentFollowingBinding
import com.slepetbangkit.cinematch.databinding.FragmentOtherFollowListBinding
import com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist.SelfFollowListViewModel
import com.slepetbangkit.cinematch.view.profile.followlist.selffollowlist.adapter.SelfFollowListItemAdapter

class SelfFollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding: FragmentFollowingBinding get() = _binding!!
    private lateinit var selfFollowListViewModel: SelfFollowListViewModel
    private lateinit var selfFollowListItemAdapter: SelfFollowListItemAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.navigation_self_profile)

        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        selfFollowListViewModel = ViewModelProvider(navBackStackEntry)[SelfFollowListViewModel::class.java]
        selfFollowListItemAdapter = SelfFollowListItemAdapter()
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.followingRv.apply {
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
            if (it.followings.isEmpty()) {
                binding.tvNoFollowing.visibility = View.VISIBLE
            } else {
                binding.tvNoFollowing.visibility = View.GONE
            }
            selfFollowListItemAdapter.submitList(it.followings)
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