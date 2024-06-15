package com.slepetbangkit.cinematch.view.activity

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
import com.slepetbangkit.cinematch.data.preferences.dataStore
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
    private lateinit var activityAdapter: ActivityAdapter
    private lateinit var navController: NavController
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
        activityAdapter = ActivityAdapter()
        navController = findNavController()

        lifecycleScope.launch {
            sessionUsername = sessionRepository.getUsername()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.activityRv.layoutManager = LinearLayoutManager(context)
        binding.activityRv.adapter = activityAdapter

        activityViewModel.activity.observe(viewLifecycleOwner) { activityResponse ->
            activityAdapter.submitList(activityResponse.activities)

            activityAdapter.setOnItemClickCallback(object : ActivityAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ActivitiesItem) {
                    if (data.type == "FOLLOWED_USER") {
                        val bundle = if (data.followedUsername == sessionUsername) {
                            Bundle().apply {
                                putString("username", data.username)
                            }
                        } else {
                            Bundle().apply {
                                putString("username", data.followedUsername)
                            }
                        }
                        navController.navigate(R.id.action_navigation_activity_to_navigation_other_profile, bundle)
                    } else {
                        return
                    }
                }
            })
        }

    }
}