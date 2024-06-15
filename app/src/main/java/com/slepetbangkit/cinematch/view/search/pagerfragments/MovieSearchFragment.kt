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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.MovieSearchResponseItem
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.databinding.FragmentMovieSearchBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.SearchMovieViewModelFactory
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchMovieViewModel
import com.slepetbangkit.cinematch.view.search.adapter.MovieAdapter
import kotlinx.coroutines.launch

class MovieSearchFragment : Fragment() {
    private var _binding: FragmentMovieSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var movieRepository: MovieRepository
    private lateinit var factory: SearchMovieViewModelFactory
    private lateinit var searchMovieViewModel: SearchMovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var navController: NavController

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

        sessionRepository = Injection.provideSessionRepository(requireContext())
        movieRepository = Injection.provideMovieRepository(requireContext())
        factory = SearchMovieViewModelFactory.getInstance(sessionRepository, movieRepository)
        searchMovieViewModel = ViewModelProvider(this, factory)[SearchMovieViewModel::class.java]
        navController = findNavController()

        setupRecyclerView()
        setupSearchInput()

        searchMovieViewModel.searchMovieResult.observe(viewLifecycleOwner) { movies ->
            movieAdapter.submitList(movies)

            if (movies.isEmpty()) {
                binding.noResultsTextView.visibility = View.VISIBLE
            } else {
                binding.noResultsTextView.visibility = View.GONE
            }
        }

        searchMovieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) { binding.noResultsTextView.visibility = View.GONE }
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

        movieAdapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: MovieSearchResponseItem) {
                data.tmdbId.let { tmdbId ->
                    val bundle = Bundle().apply {
                        putInt("tmdbId", tmdbId)
                    }
                    navController.navigate(R.id.action_navigation_search_to_movieDetailsFragment, bundle)
                }
            }
        })
    }

    private fun setupSearchInput() {
        binding.searchTv.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                lifecycleScope.launch {
                    val query = binding.searchTv.text.toString().trim()
                    if (query.isNotEmpty()) {
                        searchMovieViewModel.searchMovies(query)
                        v.clearFocus()
                        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
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
