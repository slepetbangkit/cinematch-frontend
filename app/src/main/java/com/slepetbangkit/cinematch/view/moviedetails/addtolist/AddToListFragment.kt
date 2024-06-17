package com.slepetbangkit.cinematch.view.moviedetails.addtolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentAddToListBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.MovieListViewModelFactory
import com.slepetbangkit.cinematch.factories.MovieViewModelFactory
import com.slepetbangkit.cinematch.view.moviedetails.MovieDetailsViewModel

class AddToListFragment : Fragment() {
    private var _binding: FragmentAddToListBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var movieListRepository: MovieListRepository
    private lateinit var movieListFactory: MovieListViewModelFactory
    private lateinit var movieListAdapter: AddToListAdapter
    private lateinit var movielistViewModel: AddMovieToListViewModel
    private lateinit var movieFactory: MovieViewModelFactory
    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var movieRepository: MovieRepository
    private lateinit var navController: NavController
    private var tmdbId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tmdbId = arguments?.getInt("tmdbId") ?: 0

        _binding = FragmentAddToListBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())

        movieListRepository = Injection.provideMovieListRepository(requireContext())
        movieListFactory = MovieListViewModelFactory.getInstance(sessionRepository, movieListRepository)
        movielistViewModel = ViewModelProvider(requireActivity(), movieListFactory)[AddMovieToListViewModel::class.java]

        movieRepository = Injection.provideMovieRepository(requireContext())
        movieFactory = MovieViewModelFactory.getInstance(sessionRepository, movieRepository)
        movieDetailsViewModel = ViewModelProvider(this, movieFactory)[MovieDetailsViewModel::class.java]
        movieFactory.updateTmdbId(tmdbId)

        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            navController.navigateUp()
        }

        binding.movieListRv.apply {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }

        movieDetailsViewModel.movieDetail.observe(viewLifecycleOwner) { movieDetails ->
            movieListAdapter = AddToListAdapter(movieDetails.inPlaylists) { playlist, isChecked ->
                if (isChecked) {
                    movielistViewModel.addMovieToList(playlist.id, tmdbId)
                    movieDetailsViewModel.fetchMovieDetails()
                } else {
                    movielistViewModel.deleteMovieFromList(playlist.id, tmdbId)
                    movieDetailsViewModel.fetchMovieDetails()
                }
            }
            binding.movieListRv.adapter = movieListAdapter

            movielistViewModel.movieListDetails.observe(viewLifecycleOwner) { playlists ->
                movieListAdapter.submitList(playlists)
            }
        }

        movielistViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
