package com.slepetbangkit.cinematch.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentSearchBinding
import com.slepetbangkit.cinematch.helpers.ViewModelFactory
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchMovieViewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var factory: ViewModelFactory
    private lateinit var searchMovieViewModel: SearchMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        factory = ViewModelFactory.getInstance(sessionRepository)
        searchMovieViewModel = ViewModelProvider(this, factory)[SearchMovieViewModel::class.java]

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        context?.let {
            val adapter = SearchViewAdapter(it as FragmentActivity)
            viewPager.adapter = adapter
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Movie"
                1 -> tab.text = "User"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
