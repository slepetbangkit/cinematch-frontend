package com.slepetbangkit.cinematch.view.search.pagerFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.data.local.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.databinding.FragmentMovieSearchBinding
import com.slepetbangkit.cinematch.helpers.ViewModelFactory
import com.slepetbangkit.cinematch.view.search.SearchMovieViewModel
import com.slepetbangkit.cinematch.view.search.adapter.MovieAdapter

class MovieSearchFragment : Fragment() {
    private var _binding: FragmentMovieSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionPrefs: SessionPreferences
    private lateinit var searchMovieViewModel: SearchMovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionPrefs = SessionPreferences.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory.getInstance(sessionPrefs)
        val viewModel: SearchMovieViewModel by viewModels { viewModelFactory }
        searchMovieViewModel = viewModel

        setupRecyclerView()
        setupSearchInput()

        searchMovieViewModel.searchMovieResult.observe(viewLifecycleOwner) { movies ->
            movieAdapter.submitList(movies)
        }

        searchMovieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Handle loading state if necessary
        }

        searchMovieViewModel.error.observe(viewLifecycleOwner) { error ->
            Log.e("MovieSearchFragment", "Error: $error")
            // Handle error state if necessary, e.g., show a Toast or Snackbar
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        binding.movieRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
        }
    }

    private fun setupSearchInput() {
        binding.searchTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    searchMovieViewModel.getSearchMovies(query)
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        searchMovieViewModel.clearSearchResults()
        binding.searchTv.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
