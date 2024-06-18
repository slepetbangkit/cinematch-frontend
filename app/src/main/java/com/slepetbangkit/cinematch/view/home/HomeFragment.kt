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

        val recommdedMoviesAdapter = LargeMoviePosterAdapter()
        val freindsMoviesAdapter = FriendsMoviePosterAdapter()
        val verdictCardAdapter = VerdictCardAdapter()
        val topRatedMoviesAdapter = TopRatedMoviePosterAdapter()

        binding.recommendedMoviesRv.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = recommdedMoviesAdapter
        }

        binding.friendsLikesRv.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = freindsMoviesAdapter
        }

        binding.theirVerdictRv.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = verdictCardAdapter
        }

        binding.topRatedMoviesRv.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = topRatedMoviesAdapter
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {

        }

        homeViewModel.response.observe(viewLifecycleOwner) {
            recommdedMoviesAdapter.submitList(it.recommended)
            freindsMoviesAdapter.submitList(it.friends)
            verdictCardAdapter.submitList(it.verdict)
            topRatedMoviesAdapter.submitList(it.toprated)
        }
    }
}