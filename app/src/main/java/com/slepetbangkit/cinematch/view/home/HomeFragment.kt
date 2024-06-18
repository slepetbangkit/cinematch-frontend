package com.slepetbangkit.cinematch.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.FriendsItem
import com.slepetbangkit.cinematch.data.remote.response.RecommendedItem
import com.slepetbangkit.cinematch.data.remote.response.TopRatedItem
import com.slepetbangkit.cinematch.data.remote.response.VerdictItem
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentHomeBinding
import com.slepetbangkit.cinematch.view.home.adapter.FriendsMoviePosterAdapter
import com.slepetbangkit.cinematch.view.home.adapter.LargeMoviePosterAdapter
import com.slepetbangkit.cinematch.view.home.adapter.TopRatedMoviePosterAdapter
import com.slepetbangkit.cinematch.view.home.adapter.VerdictCardAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private val homeViewModel by viewModels<HomeViewModel>()
    private val recommdedMoviesAdapter = LargeMoviePosterAdapter()
    private val freindsMoviesAdapter = FriendsMoviePosterAdapter()
    private val verdictCardAdapter = VerdictCardAdapter()
    private val topRatedMoviesAdapter = TopRatedMoviePosterAdapter()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()
    }

    private fun setupView() {
        binding.recommendedMoviesRv.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = recommdedMoviesAdapter
        }
        recommdedMoviesAdapter.setOnItemClickCallback(object : LargeMoviePosterAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RecommendedItem) {
                val bundle = Bundle()
                bundle.putInt("tmdbId", data.tmdbId)
                navController.navigate(R.id.action_navigation_home_to_navigation_movie_details, bundle)
            }
        })

        binding.friendsLikesRv.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = freindsMoviesAdapter
        }
        freindsMoviesAdapter.setOnItemClickCallback(object : FriendsMoviePosterAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FriendsItem) {
                val bundle = Bundle()
                bundle.putInt("tmdbId", data.tmdbId)
                navController.navigate(R.id.action_navigation_home_to_navigation_movie_details, bundle)
            }
        })

        binding.theirVerdictRv.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = verdictCardAdapter
        }
        verdictCardAdapter.setOnItemClickCallback(object : VerdictCardAdapter.OnItemClickCallback {
            override fun onItemClicked(data: VerdictItem) {
//                val bundle = Bundle()
//                bundle.putString("reviewId", data.reviewId)
//                navController.navigate(R.id.action_navigation_home_to_navigation_review_detail, bundle)
                return
            }
        })

        binding.topRatedMoviesRv.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = topRatedMoviesAdapter
        }
        topRatedMoviesAdapter.setOnItemClickCallback(object : TopRatedMoviePosterAdapter.OnItemClickCallback {
            override fun onItemClicked(data: TopRatedItem) {
                val bundle = Bundle()
                bundle.putInt("tmdbId", data.tmdbId)
                navController.navigate(R.id.action_navigation_home_to_navigation_movie_details, bundle)
            }
        })
    }

    private fun setupObservers() {
        homeViewModel.isLoading.observe(viewLifecycleOwner) {

        }

        homeViewModel.response.observe(viewLifecycleOwner) {
            recommdedMoviesAdapter.submitList(it.data.recommended)
            freindsMoviesAdapter.submitList(it.data.friends)
            verdictCardAdapter.submitList(it.data.verdict)
            topRatedMoviesAdapter.submitList(it.data.topRated)
        }
    }
}