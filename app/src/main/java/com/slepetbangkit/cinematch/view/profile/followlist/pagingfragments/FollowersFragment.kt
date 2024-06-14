package com.slepetbangkit.cinematch.view.profile.followlist.pagingfragments

import android.os.Bundle
import android.util.Log
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
import com.slepetbangkit.cinematch.data.remote.response.UsersItem
import com.slepetbangkit.cinematch.databinding.FragmentFollowersBinding
import com.slepetbangkit.cinematch.view.profile.followlist.FollowListViewModel
import com.slepetbangkit.cinematch.view.profile.followlist.adapter.FollowListAdapter
import com.slepetbangkit.cinematch.view.profile.followlist.adapter.FollowListItemAdapter
import com.slepetbangkit.cinematch.view.search.adapter.UserAdapter

class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding: FragmentFollowersBinding get() = _binding!!
    private lateinit var followListViewModel: FollowListViewModel
    private lateinit var navController: NavController
    private lateinit var followListItemAdapter: FollowListItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.navigation_profile)

        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        followListViewModel = ViewModelProvider(navBackStackEntry)[FollowListViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followListItemAdapter = FollowListItemAdapter()
        binding.followersRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = followListItemAdapter
        }
        followListItemAdapter.setOnItemClickCallback(object : FollowListItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FollowListItem) {
                data.username?.let { username ->
                    val bundle = Bundle().apply {
                        putString("username", username)
                    }
                    navController.navigate(R.id.action_navigation_follow_list_to_navigation_profile, bundle)
                }
            }
        })

        followListViewModel.followList.observe(viewLifecycleOwner) {
            followListItemAdapter.submitList(it.followers)
        }
    }
}