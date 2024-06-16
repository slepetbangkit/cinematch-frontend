package com.slepetbangkit.cinematch.view.moviedetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.preferences.dataStore
import com.slepetbangkit.cinematch.data.remote.response.SimilarMoviesItem
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentMovieDetailsBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.MovieViewModelFactory

class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var movieRepository: MovieRepository
    private lateinit var factory: MovieViewModelFactory
    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tmdbId = arguments?.getInt("tmdbId") ?: 0

        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        movieRepository = Injection.provideMovieRepository(requireContext())

        factory = MovieViewModelFactory.getInstance(sessionRepository, movieRepository)
        factory.updateTmdbId(tmdbId)

        movieDetailsViewModel = ViewModelProvider(this, factory)[MovieDetailsViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        fetchMovieDetails() // Fetch data when the view is created
    }

    override fun onResume() {
        super.onResume()
        fetchMovieDetails() // Fetch data when the fragment resumes
    }

    private fun observeViewModel() {
        movieDetailsViewModel.movieDetail.observe(viewLifecycleOwner) { movie ->
            movie.apply {
                binding.movieDetailsView.setMovieTitle(title)
                binding.movieDetailsView.setReleaseYear(releaseDate)
                binding.movieDetailsView.setDirector(director)
                binding.movieDetailsView.setMoviePoster(posterUrl)
                binding.movieDetailsView.setSynopsis(description)
                binding.movieDetailsView.setVerdict(overallSentiment)
                binding.movieDetailsView.setCast(cast)
                binding.movieDetailsView.setCrew(crew)
                trailerLink?.let { binding.movieDetailsView.setTrailerLink(it) } ?: binding.movieDetailsView.setTrailerLink(null)
                binding.movieDetailsView.setTrailerBackdrop(backdropUrl)
                binding.movieDetailsView.setOriginCountries(originCountries)
                binding.movieDetailsView.setGenres(genres)
                binding.movieDetailsView.setLanguage(languages)
                binding.movieDetailsView.setReleaseDate(releaseDate)
                binding.movieDetailsView.setRuntime(runtime)
                binding.movieDetailsView.setReviewButtonClickListener(tmdbId, title, releaseDate)
                setupSimilarMovies(similarMovies)
            }
        }

        movieDetailsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.movieDetailsView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun fetchMovieDetails() {
        val tmdbId = arguments?.getInt("tmdbId") ?: 0
        movieDetailsViewModel.fetchMovieDetails(tmdbId)
    }

    private fun setupSimilarMovies(similarMovies: List<SimilarMoviesItem?>) {
        binding.movieDetailsView.setSimilarMovies(similarMovies) { similarMovie ->
            val bundle = Bundle().apply {
                similarMovie.tmdbId?.let { putInt("tmdbId", it) }
            }
            navController.navigate(R.id.action_movieDetailsFragment_self, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val MOVIE_ID = "movie_id"
    }
}
