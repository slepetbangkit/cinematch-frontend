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
import com.slepetbangkit.cinematch.data.preferences.dataStore
import com.slepetbangkit.cinematch.data.remote.response.ReviewsItem
import com.slepetbangkit.cinematch.data.repository.MovieRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.databinding.FragmentReviewBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.MovieViewModelFactory

class ReviewFragment : Fragment() {
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var movieRepository: MovieRepository
    private lateinit var factory: MovieViewModelFactory
    private lateinit var reviewViewModel: ReviewViewModel
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
        sessionRepository = Injection.provideSessionRepository(requireContext())
        movieRepository = Injection.provideMovieRepository(requireContext())
        factory = MovieViewModelFactory.getInstance(sessionRepository, movieRepository, tmdbId)

        reviewViewModel = ViewModelProvider(this, factory)[ReviewViewModel::class.java]
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
        reviewViewModel.apply {
            movieReviews.observe(viewLifecycleOwner) { reviews ->
                reviewAdapter.submitList(reviews.reviews)
                binding.tvNoReviews.visibility = if (reviews.reviews.isEmpty()) View.VISIBLE else View.GONE
            }
            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.reviewContainer.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }
    }

    private fun fetchReviews() {
        reviewViewModel.getMovieReviews()
    }

    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter()
        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reviewAdapter
        }
        reviewAdapter.setOnItemClickCallback(object : ReviewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ReviewsItem) {
                navigateToReviewDetail(data.id)
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
