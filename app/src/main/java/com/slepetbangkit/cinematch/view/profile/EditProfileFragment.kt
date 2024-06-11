package com.slepetbangkit.cinematch.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentEditProfileBinding
import com.slepetbangkit.cinematch.helpers.ProfileViewModelFactory
import com.slepetbangkit.cinematch.helpers.ViewModelFactory

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var factory: ViewModelFactory
    private lateinit var editProfileViewModel: EditProfileViewModel
    private lateinit var sharedProfileViewModel: SharedProfileViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        factory = ViewModelFactory.getInstance(sessionRepository)
        editProfileViewModel = ViewModelProvider(this, factory)[EditProfileViewModel::class.java]
        sharedProfileViewModel = ViewModelProvider(requireActivity())[SharedProfileViewModel::class.java]
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
        editProfileViewModel.profile.observe(viewLifecycleOwner) {
            binding.edtUname.setText(it.username)
            binding.edtBio.setText(it.bio)
        }

        editProfileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSave.text = if (isLoading) "" else "Save"
            binding.btnSave.background.alpha = if (isLoading) 77 else 255
        }

        editProfileViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                AlertDialog.Builder(requireContext(), R.style.AlertDialog)
                    .setTitle(error)
                    .setPositiveButton("Try Again") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        editProfileViewModel.message.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                AlertDialog.Builder(requireContext(), R.style.AlertDialog)
                    .setTitle(message.message)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        navController.navigateUp()
                    }
                    .show()
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
            editProfileViewModel.updateProfile()
            sharedProfileViewModel.setProfileUpdated(true)
        }
    }

    private fun setupTextChangedListeners() {
        binding.edtUname.addTextChangedListener { text ->
            editProfileViewModel.setNewUsername(text.toString())
        }

        binding.edtBio.addTextChangedListener { text ->
            editProfileViewModel.setNewBio(text.toString())
        }
    }
}