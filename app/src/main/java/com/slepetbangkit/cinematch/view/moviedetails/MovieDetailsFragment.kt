package com.slepetbangkit.cinematch.view.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.remote.response.SimilarMoviesItem
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentMovieDetailsBinding
import com.slepetbangkit.cinematch.helpers.MovieViewModelFactory

class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var factory: MovieViewModelFactory
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tmdbId = arguments?.getInt("tmdbId") ?: 0

        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        factory = MovieViewModelFactory.getInstance(sessionRepository, tmdbId)

        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        movieViewModel.movieDetail.observe(viewLifecycleOwner) { movie ->
            movie.apply {
                title?.let { binding.movieDetailsView.setMovieTitle(it) }
                releaseDate?.let { binding.movieDetailsView.setReleaseYear(it) }
                director?.let { binding.movieDetailsView.setDirector(it) }
                posterUrl?.let { binding.movieDetailsView.setMoviePoster(it) }
                description?.let { binding.movieDetailsView.setSynopsis(it) }
                rating?.let { binding.movieDetailsView.setVerdict(it) }
                cast?.let { binding.movieDetailsView.setCast(it) }
                crew?.let { binding.movieDetailsView.setCrew(it) }
                similarMovies?.let { setupSimilarMovies(it) }
                trailerLink?.let { binding.movieDetailsView.setTrailerLink(it) } ?: binding.movieDetailsView.setTrailerLink(null)
                backdropUrl?.let { binding.movieDetailsView.setTrailerBackdrop(it) }
                originCountries?.let { binding.movieDetailsView.setOriginCountries(it) }
                genres?.let { binding.movieDetailsView.setGenres(it) }
                languages?.let { binding.movieDetailsView.setLanguage(it) }
                releaseDate?.let { binding.movieDetailsView.setReleaseDate(it) }
                tmdbId?.let { id ->
                    title?.let { title ->
                        releaseDate?.let { releaseDate ->
                            binding.movieDetailsView.setReviewButtonClickListener(id, title, releaseDate)
                        }
                    }
                }
                runtime?.let { binding.movieDetailsView.setRuntime(it) }
            }
        }

        movieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.movieDetailsView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
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
