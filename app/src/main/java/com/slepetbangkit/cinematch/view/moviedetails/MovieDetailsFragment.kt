package com.slepetbangkit.cinematch.view.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
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
        var tmdbId = arguments?.getInt("tmdbId")
        if (tmdbId == null) {
            tmdbId = 0
        }

        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        factory = MovieViewModelFactory.getInstance(sessionRepository, tmdbId)

        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel.movieDetail.observe(viewLifecycleOwner) {
            it.title?.let { title -> binding.movieDetailsView.setMovieTitle(title) }
            it.releaseDate?.let { releaseDate -> binding.movieDetailsView.setReleaseYear(releaseDate) }
            it.director?.let { director -> binding.movieDetailsView.setDirector(director) }
            it.posterUrl?.let { posterUrl -> binding.movieDetailsView.setMoviePoster(posterUrl) }
            it.description?.let { description -> binding.movieDetailsView.setSynopsis(description) }
            it.rating?.let { rating -> binding.movieDetailsView.setVerdict(rating) }
            it.cast?.let { cast -> binding.movieDetailsView.setCast(cast) }
            it.crew?.let { crew -> binding.movieDetailsView.setCrew(crew) }
            it.similarMovies?.let { similarMovies ->
                binding.movieDetailsView.setSimilarMovies(similarMovies) { similarMovie ->
                    val bundle = Bundle().apply {
                        similarMovie.tmdbId?.let { it1 -> putInt("tmdbId", it1) }
                    }
                    navController.navigate(R.id.action_movieDetailsFragment_self, bundle)
                }
            }
            if (it.trailerLink.isNullOrEmpty()) {
                binding.movieDetailsView.setTrailerLink(null)
            } else {
                it.trailerLink.let { trailerLink -> binding.movieDetailsView.setTrailerLink(trailerLink) }
            }
            it.backdropUrl?.let { backdropUrl -> binding.movieDetailsView.setTrailerBackdrop(backdropUrl) }
            it.originCountries?.let { originCountries -> binding.movieDetailsView.setOriginCountries(originCountries) }
            it.genres?.let { genres -> binding.movieDetailsView.setGenres(genres) }
            it.languages?.let { languages -> binding.movieDetailsView.setLanguage(languages) }
            it.releaseDate?.let { releaseDate -> binding.movieDetailsView.setReleaseDate(releaseDate) }
            it.tmdbId?.let { tmdbId -> binding.movieDetailsView.setReviewButtonClickListener(tmdbId) }
            it.runtime?.let { runtime -> binding.movieDetailsView.setRuntime(runtime)}
        }

        movieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.movieDetailsView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    companion object {
        const val MOVIE_ID = "movie_id"
    }
}