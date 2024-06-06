package com.slepetbangkit.cinematch.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.slepetbangkit.cinematch.data.local.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.databinding.FragmentSearchBinding
import com.slepetbangkit.cinematch.helpers.ViewModelFactory

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchMovieViewModel: SearchMovieViewModel
    private lateinit var sessionPrefs: SessionPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionPrefs = SessionPreferences.getInstance(requireContext().dataStore)
        searchMovieViewModel = ViewModelFactory.getInstance(sessionPrefs).create(SearchMovieViewModel::class.java)

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

    override fun onPause() {
        searchMovieViewModel.clearSearchResults()
        super.onPause()
    }

    override fun onDestroyView() {
        searchMovieViewModel.clearSearchResults()
        _binding = null
        super.onDestroyView()
    }
}
