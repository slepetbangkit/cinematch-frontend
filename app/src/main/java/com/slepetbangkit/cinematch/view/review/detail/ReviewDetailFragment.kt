package com.slepetbangkit.cinematch.view.review.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentReviewDetailBinding
import com.slepetbangkit.cinematch.helpers.ReviewDetailsViewModelFactory

class ReviewDetailFragment : Fragment() {
    private var _binding: FragmentReviewDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var factory: ReviewDetailsViewModelFactory
    private lateinit var reviewDetailViewModel: ReviewDetailViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var reviewId = arguments?.getString("reviewId")
        val movieTitle = arguments?.getString("movieTitle")
        val releaseDate = arguments?.getString("releaseDate")

        if (reviewId == null) {
            reviewId = ""
        }

        _binding = FragmentReviewDetailBinding.inflate(inflater, container, false)
        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        factory = ReviewDetailsViewModelFactory.getInstance(sessionRepository, reviewId)

        reviewDetailViewModel = ViewModelProvider(this, factory)[ReviewDetailViewModel::class.java]
        navController = findNavController()

        binding.apply {
            tvTitle.text = movieTitle
            tvYear.text = releaseDate
            btnBack.setOnClickListener {
                navController.navigateUp()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reviewDetailViewModel.reviewDetails.observe(viewLifecycleOwner) {
            it.data?.username?.let { username -> binding.tvUsername.text = username }
            it.data?.description?.let { description -> binding.tvReview.text = description }
            it.data?.rating?.let { rating -> binding.tvRating.text = rating.toString() }
        }

        reviewDetailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.reviewDetailContainer.visibility = if (isLoading) View.GONE else View.VISIBLE
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
