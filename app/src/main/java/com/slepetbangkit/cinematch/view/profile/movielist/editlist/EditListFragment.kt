package com.slepetbangkit.cinematch.view.profile.movielist.editlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentEditListBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.MovieListViewModelFactory
import com.slepetbangkit.cinematch.view.profile.movielist.MovieListViewModel
import kotlinx.coroutines.launch

class EditListFragment : Fragment() {
    private var _binding: FragmentEditListBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var movieListRepository: MovieListRepository
    private lateinit var factory: MovieListViewModelFactory
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var navController: NavController
    private var listId: String = ""
    private var newName: String = ""
    private var newDesc: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listId = arguments?.getString("listId") ?: ""

        _binding = FragmentEditListBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        movieListRepository = Injection.provideMovieListRepository(requireContext())
        factory = MovieListViewModelFactory.getInstance(sessionRepository, movieListRepository)
        movieListViewModel = ViewModelProvider(this, factory)[MovieListViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupViews()
        setupTextChangedListeners()
    }

    private fun setupObservers() {
        movieListViewModel.movieListDetails.observe(viewLifecycleOwner) {
            binding.edtName.setText(it.title)
            binding.edtDesc.setText(it.description)
        }

        movieListViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSave.text = if (isLoading) "" else "Save"
            binding.btnSave.background.alpha = if (isLoading) 77 else 255
        }

        movieListViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                showAlertDialog(
                    "Update Failed",
                    "Something went wrong. Please try again.",
                    "Try Again",
                    null
                )
            }
        }

        movieListViewModel.isEdited.observe(viewLifecycleOwner) { isEdited ->
            if (isEdited) {
                showAlertDialog(
                    "Update Successful",
                    "Your movie list has been successfully updated.",
                    "OK"
                ) {
                    navController.navigateUp()
                }
            }
        }

        movieListViewModel.isDeleted.observe(viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                navController.navigateUp()
                navController.navigateUp()
            }
        }
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            AlertDialog.Builder(requireContext(), R.style.AlertDialog)
                .setTitle("Discard Changes")
                .setMessage("Are you sure you want to discard changes?")
                .setPositiveButton("Yes") { _, _ ->
                    navController.navigateUp()
                }
                .setNegativeButton("No", null)
                .show()
        }

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                movieListViewModel.editMovieList(listId, newName, newDesc)
            }
        }

        binding.btnDeleteList.setOnClickListener {
            showDeleteListConfirmationDialog { movieListViewModel.deleteMovieListById(listId) }
        }
    }

    private fun setupTextChangedListeners() {
        binding.edtName.addTextChangedListener { text ->
            newName = text.toString()
        }

        binding.edtDesc.addTextChangedListener { text ->
            newDesc = text.toString()
        }
    }

    private fun showAlertDialog(title: String, msg: String, positiveButtonText: String, onPositiveClick: (() -> Unit)?) {
        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
                dialog.dismiss()
                onPositiveClick?.invoke()
            }
            .show()
    }

    private fun showDeleteListConfirmationDialog(onConfirm: () -> Unit) {
        android.app.AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle("Delete List")
            .setMessage("Are you sure you want to delete this list?")
            .setPositiveButton("Yes") { _, _ -> onConfirm() }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
