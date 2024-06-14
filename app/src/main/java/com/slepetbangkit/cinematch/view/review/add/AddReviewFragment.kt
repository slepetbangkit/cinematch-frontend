package com.slepetbangkit.cinematch.view.review.add

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
import com.slepetbangkit.cinematch.data.remote.response.Movie
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentAddReviewBinding
import com.slepetbangkit.cinematch.helpers.MovieViewModelFactory
import com.slepetbangkit.cinematch.view.review.MovieReviewsViewModel

class AddReviewFragment : Fragment() {
    private var _binding: FragmentAddReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var factory: MovieViewModelFactory
    private lateinit var addReviewViewModel: AddReviewViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var tmdbId = arguments?.getInt("tmdbId")
        val movieTitle = arguments?.getString("movieTitle")
        val releaseDate = arguments?.getString("releaseDate")

        if (tmdbId == null) {
            tmdbId = 0
        }

        _binding = FragmentAddReviewBinding.inflate(inflater, container, false)
        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        factory = MovieViewModelFactory.getInstance(sessionRepository, tmdbId)

        addReviewViewModel = ViewModelProvider(this, factory)[AddReviewViewModel::class.java]
        navController = findNavController()

        binding.apply {
            btnBack.setOnClickListener { navController.navigateUp() }
            btnAddReview.setOnClickListener { handleAddReview() }
            tvMovieTitle.text = movieTitle
            tvMovieYear.text = releaseDate
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addReviewViewModel.reviewResponse.observe(viewLifecycleOwner) { reviewResponse ->
            if (reviewResponse != null) {
                navController.navigateUp()
            }
        }
        addReviewViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun handleAddReview() {
        val review = binding.edtReview.text.toString()

        addReviewViewModel.addReview(review)
    }
}
