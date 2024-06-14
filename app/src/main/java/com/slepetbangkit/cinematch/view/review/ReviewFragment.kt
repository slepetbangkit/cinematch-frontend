package com.slepetbangkit.cinematch.view.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.remote.response.ReviewsItem
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentReviewBinding
import com.slepetbangkit.cinematch.helpers.MovieViewModelFactory
import com.slepetbangkit.cinematch.view.review.MovieReviewsViewModel

class ReviewFragment : Fragment() {
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieReviewsViewModel: MovieReviewsViewModel
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var navController: NavController
    private var movieTitle: String? = ""
    private var releaseDate: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tmdbId = arguments?.getInt("tmdbId") ?: 0
        movieTitle = arguments?.getString("movieTitle")
        releaseDate = arguments?.getString("releaseDate")

        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        val sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        val factory = MovieViewModelFactory.getInstance(sessionRepository, tmdbId)

        movieReviewsViewModel = ViewModelProvider(this, factory)[MovieReviewsViewModel::class.java]
        navController = findNavController()

        setupViews(tmdbId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        fetchReviews()
    }

    override fun onResume() {
        super.onResume()
        fetchReviews()
    }

    private fun setupViews(tmdbId: Int) {
        binding.apply {
            btnBack.setOnClickListener { navController.navigateUp() }
            fab.setOnClickListener { navigateToAddReview(tmdbId) }
        }
        setupRecyclerView()
    }

    private fun observeViewModel() {
        movieReviewsViewModel.apply {
            movieReviews.observe(viewLifecycleOwner) { reviews ->
                reviewAdapter.submitList(reviews.reviews)
                binding.tvNoReviews.visibility = if (reviews.reviews.isNullOrEmpty()) View.VISIBLE else View.GONE
            }
            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.reviewContainer.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }
    }

    private fun fetchReviews() {
        movieReviewsViewModel.fetchMovieReviews()
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
                    navigateToReviewDetail(reviewId)
                }
            }
        })
    }

    private fun navigateToAddReview(tmdbId: Int) {
        val bundle = Bundle().apply {
            putInt("tmdbId", tmdbId)
            putString("movieTitle", movieTitle)
            putString("releaseDate", releaseDate)
        }
        navController.navigate(R.id.action_reviewFragment_to_addReviewFragment, bundle)
    }

    private fun navigateToReviewDetail(reviewId: String) {
        val bundle = Bundle().apply {
            putString("reviewId", reviewId)
            putString("movieTitle", movieTitle)
            putString("releaseDate", releaseDate)
        }
        navController.navigate(R.id.action_reviewFragment_to_reviewDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
