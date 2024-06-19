package com.slepetbangkit.cinematch.view.activity

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
import com.slepetbangkit.cinematch.data.remote.response.ActivitiesItem
import com.slepetbangkit.cinematch.data.repository.ActivityRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentActivityBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.ActivityViewModelFactory
import kotlinx.coroutines.launch

class ActivityFragment : Fragment() {
    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var activityRepository: ActivityRepository
    private lateinit var factory: ActivityViewModelFactory
    private lateinit var activityViewModel: ActivityViewModel
    private lateinit var navController: NavController
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var activityAdapter: ActivityAdapter
    private var sessionUsername = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        activityRepository = Injection.provideActivityRepository(requireContext())
        factory = ActivityViewModelFactory.getInstance(sessionRepository, activityRepository)
        activityViewModel = ViewModelProvider(this, factory)[ActivityViewModel::class.java]
        navController = findNavController()

        lifecycleScope.launch {
            sessionUsername = sessionRepository.getUsername()
            setupView()
            setupObservers()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchActivities()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchActivities() {
        lifecycleScope.launch {
            activityViewModel.getActivities()
        }
    }

    private fun setupView() {
        layoutManager = LinearLayoutManager(context)
        activityAdapter = ActivityAdapter(sessionUsername)

        binding.activityRv.layoutManager = layoutManager
        binding.activityRv.adapter = activityAdapter
        activityAdapter.setOnItemClickCallback(object : ActivityAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ActivitiesItem) {
                when (data.type) {
                    "LIKED_MOVIE" -> {
                        val bundle = Bundle().apply {
                            data.movieTmdbId?.let { putInt("tmbdId", it) }
                        }
                        navController.navigate(R.id.action_navigation_activity_to_navigation_movie_details, bundle)
                    }
                    "REVIEWED_MOVIE" -> {
                        val bundle = Bundle().apply {
                            data.movieTmdbId?.let { putInt("tmdbId", it) }
                        }
                        navController.navigate(R.id.action_navigation_activity_to_navigation_movie_details, bundle)
                    }
                    "FOLLOWED_USER" -> {
                        val bundle = if (data.followedUsername == sessionUsername) {
                            Bundle().apply {
                                putString("username", data.username)
                            }
                        } else {
                            Bundle().apply {
                                data.followedUsername?.let { putString("username", it) }
                            }
                        }
                        navController.navigate(R.id.action_navigation_activity_to_navigation_other_profile, bundle)
                    }
                    "ADDED_MOVIE_TO_PLAYLIST" -> {
                        val bundle = Bundle().apply {
                            data.movieTmdbId?.let { putInt("tmdbId", it) }
                        }
                        navController.navigate(R.id.action_navigation_activity_to_navigation_movie_details, bundle)
                    }
                }
            }
        })
    }

    private fun setupObservers() {
        activityViewModel.activity.observe(viewLifecycleOwner) { activityResponse ->
            if (activityResponse.activities.isEmpty()) {
                binding.tvNoActivities.visibility = View.VISIBLE
            } else {
                binding.tvNoActivities.visibility = View.GONE
            }

            activityAdapter.submitList(activityResponse.activities)
        }

        activityViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.activityRv.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }
}