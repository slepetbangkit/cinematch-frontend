package com.slepetbangkit.cinematch.view.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.slepetbangkit.cinematch.databinding.ActivityMovieDetailsBinding
import com.slepetbangkit.cinematch.databinding.FragmentMovieDetailsBinding
import com.slepetbangkit.cinematch.databinding.ModalAddToListBinding

class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        binding.addToListIb.setOnClickListener {
            showAddToListModal()
        }

        return binding.root
    }

    private fun showAddToListModal() {
        val dialog = BottomSheetDialog(requireContext())
        val modalBinding: ModalAddToListBinding = ModalAddToListBinding.inflate(layoutInflater)
        dialog.setContentView(modalBinding.root)
    }

    companion object {
        const val MOVIE_ID = "movie_id"
    }
}