package com.slepetbangkit.cinematch.view.activity

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
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentActivityBinding
import com.slepetbangkit.cinematch.databinding.FragmentEditProfileBinding
import com.slepetbangkit.cinematch.helpers.ViewModelFactory

class ActivityFragment : Fragment() {
    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var factory: ViewModelFactory
    private lateinit var activityViewModel: ActivityViewModel
    private lateinit var activityAdapter: ActivityAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityBinding.inflate(inflater, container, false)
        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        factory = ViewModelFactory.getInstance(sessionRepository)
        activityViewModel = ViewModelProvider(this, factory)[ActivityViewModel::class.java]
        activityAdapter = ActivityAdapter()
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.activityRv.layoutManager = LinearLayoutManager(context)
        binding.activityRv.adapter = activityAdapter

        activityViewModel.activity.observe(viewLifecycleOwner) { activityResponse ->
            activityAdapter.submitList(activityResponse.activities)
        }
    }
}