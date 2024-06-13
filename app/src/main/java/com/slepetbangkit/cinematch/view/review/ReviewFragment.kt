package com.slepetbangkit.cinematch.view.review

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.remote.response.Movie
import com.slepetbangkit.cinematch.data.remote.response.ReviewsItem
import com.slepetbangkit.cinematch.data.remote.response.SearchResponseItem
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentMovieDetailsBinding
import com.slepetbangkit.cinematch.databinding.FragmentReviewBinding
import com.slepetbangkit.cinematch.databinding.FragmentUserSearchBinding
import com.slepetbangkit.cinematch.helpers.MovieViewModelFactory
import com.slepetbangkit.cinematch.helpers.ViewModelFactory
import com.slepetbangkit.cinematch.view.moviedetails.MovieViewModel
import com.slepetbangkit.cinematch.view.search.adapter.MovieAdapter
import com.slepetbangkit.cinematch.view.search.adapter.UserAdapter
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchMovieViewModel
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchUserViewModel

class ReviewFragment : Fragment() {
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var movieReviewsViewModel: MovieReviewsViewModel
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var factory: MovieViewModelFactory
    private lateinit var navController: NavController
    private var movie: Movie? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var tmdbId = arguments?.getInt("tmdbId")
        if (tmdbId == null) {
            tmdbId = 0
        }

        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        factory = MovieViewModelFactory.getInstance(sessionRepository, tmdbId)

        movieReviewsViewModel = ViewModelProvider(this, factory)[MovieReviewsViewModel::class.java]
        navController = findNavController()

        binding.btnBack.setOnClickListener {
            navController.navigateUp()
        }

        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieReviewsViewModel.movieReviews.observe(viewLifecycleOwner) { reviews ->
            reviewAdapter.submitList(reviews.reviews)
            movie = reviews.movie

            if (reviews.reviews.isNullOrEmpty()) {
                binding.tvNoReviews.visibility = View.VISIBLE
            } else {
                binding.tvNoReviews.visibility = View.GONE
            }
        }

        movieReviewsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.reviewContainer.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter()
        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reviewAdapter
        }

        reviewAdapter.setOnItemClickCallback(object : ReviewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ReviewsItem) {
                data.id?.let { reviewId ->
                    val bundle = Bundle().apply {
                        putString("reviewId", reviewId)
                        putString("movieTitle", movie?.title.toString())
                        putString("releaseDate", formatReleaseDate(movie?.releaseDate.toString()))
                    }
                    navController.navigate(R.id.action_reviewFragment_to_reviewDetailFragment, bundle)
                }
            }
        })
    }

    private fun formatReleaseDate(releaseDate: String?): String {
        return if (releaseDate.isNullOrEmpty()){
            "Unknown"
        } else {
            releaseDate.substring(0, 4)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}