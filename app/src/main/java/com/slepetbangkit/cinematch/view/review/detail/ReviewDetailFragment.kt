package com.slepetbangkit.cinematch.view.review.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentReviewDetailBinding
import com.slepetbangkit.cinematch.helpers.ReviewDetailsViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class ReviewDetailFragment : Fragment() {
    private var _binding: FragmentReviewDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewDetailViewModel: ReviewDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val reviewId = arguments?.getString("reviewId") ?: ""
        val movieTitle = arguments?.getString("movieTitle")
        val releaseDate = arguments?.getString("releaseDate")

        _binding = FragmentReviewDetailBinding.inflate(inflater, container, false)
        val sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        val factory = ReviewDetailsViewModelFactory.getInstance(sessionRepository, reviewId)

        reviewDetailViewModel = ViewModelProvider(this, factory)[ReviewDetailViewModel::class.java]

        binding.apply {
            tvTitle.text = movieTitle
            tvYear.text = releaseDate
            btnBack.setOnClickListener { findNavController().navigateUp() }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        reviewDetailViewModel.reviewDetails.observe(viewLifecycleOwner) { review ->
            review.data?.apply {
                username?.let { binding.tvUsername.text = it }
                description?.let { binding.tvReview.text = it }
                rating?.let { binding.tvRating.text = it.toString() }
                createdAt?.let { binding.tvDate.text = formatDate(it) }
            }
        }

        reviewDetailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.reviewDetailContainer.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val date: Date? = inputFormat.parse(dateString)
        return date?.let { outputFormat.format(it) } ?: dateString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
