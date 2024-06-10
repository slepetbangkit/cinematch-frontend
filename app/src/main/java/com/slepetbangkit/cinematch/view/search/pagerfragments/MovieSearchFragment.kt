package com.slepetbangkit.cinematch.view.search.pagerfragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentMovieSearchBinding
import com.slepetbangkit.cinematch.helpers.ViewModelFactory
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchMovieViewModel
import com.slepetbangkit.cinematch.view.search.adapter.MovieAdapter

class MovieSearchFragment : Fragment() {
    private var _binding: FragmentMovieSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
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

        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory.getInstance(sessionRepository)
        val viewModel: SearchMovieViewModel by viewModels { viewModelFactory }
        searchMovieViewModel = viewModel

        setupRecyclerView()
        setupSearchInput()

        searchMovieViewModel.searchMovieResult.observe(viewLifecycleOwner) { movies ->
            movieAdapter.submitList(movies)
        }

        searchMovieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        searchMovieViewModel.error.observe(viewLifecycleOwner) { error ->
            Log.e("MovieSearchFragment", "Error: $error")
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
        binding.searchTv.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = binding.searchTv.text.toString().trim()
                if (query.isNotEmpty()) {
                    searchMovieViewModel.getSearchMovies(query)
                    v.clearFocus()
                    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
                true
            } else {
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
