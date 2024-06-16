package com.slepetbangkit.cinematch.view.profile.movielist

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
import com.slepetbangkit.cinematch.data.remote.response.MovieSearchResponseItem
import com.slepetbangkit.cinematch.data.remote.response.MoviesItem
import com.slepetbangkit.cinematch.data.remote.response.SimilarMoviesItem
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentMovieDetailsBinding
import com.slepetbangkit.cinematch.databinding.FragmentMovieListBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.MovieListViewModelFactory
import com.slepetbangkit.cinematch.factories.MovieViewModelFactory
import com.slepetbangkit.cinematch.view.moviedetails.MovieDetailsViewModel
import com.slepetbangkit.cinematch.view.search.adapter.MovieAdapter

class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var movieListRepository: MovieListRepository
    private lateinit var factory: MovieListViewModelFactory
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val listId = arguments?.getString("listId") ?: ""

        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        movieListRepository = Injection.provideMovieListRepository(requireContext())

        factory = MovieListViewModelFactory.getInstance(sessionRepository, movieListRepository)
        factory.updateListId(listId)

        movieListViewModel = ViewModelProvider(this, factory)[MovieListViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        fetchMovieList()
    }

    override fun onResume() {
        super.onResume()
        fetchMovieList()
    }

    private fun observeViewModel() {
        movieListViewModel.movieListDetails.observe(viewLifecycleOwner) { movie ->
            movie.apply {
                movieListAdapter = MovieListAdapter()
                binding.rvMovies.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = movieListAdapter
                }
                movieListAdapter.submitList(movies)

                binding.tvTitle.text = title

                movieListAdapter.setOnItemClickCallback(object : MovieListAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: MoviesItem) {
                        data.tmdbId.let { tmdbId ->
                            val bundle = Bundle().apply {
                                putInt("tmdbId", tmdbId)
                            }
                            navController.navigate(R.id.action_movieListFragment_to_movieDetailsFragment2, bundle)
                        }
                    }
                })
            }
        }

        movieListViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.rvMovies.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.btnDeleteList.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        movieListViewModel.movies.observe(viewLifecycleOwner) { movies ->
            binding.emptyTv.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun fetchMovieList() {
        val listId = arguments?.getString("listId") ?: ""
        movieListViewModel.fetchMovieListDetails(listId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
