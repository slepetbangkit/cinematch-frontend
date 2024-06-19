package com.slepetbangkit.cinematch.view.home

import android.os.Bundle
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
import com.slepetbangkit.cinematch.data.remote.response.FriendsItem
import com.slepetbangkit.cinematch.data.remote.response.RecommendedItem
import com.slepetbangkit.cinematch.data.remote.response.TopRatedItem
import com.slepetbangkit.cinematch.data.remote.response.VerdictItem
import com.slepetbangkit.cinematch.data.repository.HomeRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentHomeBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.HomeViewModelFactory
import com.slepetbangkit.cinematch.view.home.adapter.FriendsMoviePosterAdapter
import com.slepetbangkit.cinematch.view.home.adapter.LargeMoviePosterAdapter
import com.slepetbangkit.cinematch.view.home.adapter.TopRatedMoviePosterAdapter
import com.slepetbangkit.cinematch.view.home.adapter.VerdictCardAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var homeRepository: HomeRepository
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var homeViewModel: HomeViewModel
    private val recommdedMoviesAdapter = LargeMoviePosterAdapter()
    private val friendsMoviesAdapter = FriendsMoviePosterAdapter()
    private val verdictCardAdapter = VerdictCardAdapter()
    private val topRatedMoviesAdapter = TopRatedMoviePosterAdapter()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        homeRepository = Injection.provideHomeRepository(requireContext())
        homeViewModelFactory = HomeViewModelFactory.getInstance(sessionRepository, homeRepository)
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        fetchHomeMovies()
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
            it.adapter = friendsMoviesAdapter
        }
        friendsMoviesAdapter.setOnItemClickCallback(object : FriendsMoviePosterAdapter.OnItemClickCallback {
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
                val bundle = Bundle()
                bundle.putString("reviewId", data.reviewId)
                bundle.putString("movieTitle", data.title)
                bundle.putString("releaseDate", data.releaseDate.substring(0, 4))
                navController.navigate(R.id.action_navigation_home_to_navigation_review_detail, bundle)
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
        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.shimmerViewContainer.let {
                if (isLoading) {
                    it.startShimmer()
                    it.visibility = View.VISIBLE
                } else {
                    it.stopShimmer()
                    it.visibility = View.GONE
                }
            }
            binding.main.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        homeViewModel.response.observe(viewLifecycleOwner) {
            recommdedMoviesAdapter.submitList(it.data.recommended)
            if (it.data.friends.isEmpty()) {
                binding.friendsLikesTv.visibility = View.GONE
                binding.friendsLikesRv.visibility = View.GONE
                binding.divider2.visibility = View.GONE
            } else {
                binding.friendsLikesTv.visibility = View.VISIBLE
                binding.friendsLikesRv.visibility = View.VISIBLE
                binding.divider2.visibility = View.VISIBLE

                friendsMoviesAdapter.submitList(it.data.friends)
            }
            verdictCardAdapter.submitList(it.data.verdict)
            topRatedMoviesAdapter.submitList(it.data.topRated)
        }
    }

    private fun fetchHomeMovies() {
        lifecycleScope.launch {
            homeViewModel.getHomeMovies()
        }
    }
}