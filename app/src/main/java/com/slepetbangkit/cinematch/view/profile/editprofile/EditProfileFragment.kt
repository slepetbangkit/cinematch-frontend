package com.slepetbangkit.cinematch.view.profile.editprofile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.MessageResponse
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.databinding.FragmentEditProfileBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.EditProfileViewModelFactory
import com.slepetbangkit.cinematch.factories.SelfProfileViewModelFactory
import com.slepetbangkit.cinematch.util.GlideApp
import com.slepetbangkit.cinematch.view.profile.selfprofile.SelfProfileViewModel
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var factory: EditProfileViewModelFactory
    private lateinit var editProfileViewModel: EditProfileViewModel
    private lateinit var selfProfileViewModel: SelfProfileViewModel
    private lateinit var navController: NavController
    private lateinit var launcherGallery: ActivityResultLauncher<PickVisualMediaRequest>
    private var oldBio: String = ""
    private var newBio: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        userRepository = Injection.provideUserRepository(requireContext())

        factory = EditProfileViewModelFactory.getInstance(requireActivity().application, sessionRepository, userRepository)

        editProfileViewModel = ViewModelProvider(this, factory)[EditProfileViewModel::class.java]
        selfProfileViewModel = ViewModelProvider(requireActivity())[SelfProfileViewModel::class.java]
        navController = findNavController()

        launcherGallery = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                editProfileViewModel.getResultGallery(uri)
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDiscardDialog()
            }
        })

        setupObservers()
        setupViews()
        setupTextChangedListeners()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupObservers() {
        selfProfileViewModel.profile.observe(viewLifecycleOwner) {
            GlideApp.with(binding.imgProfile.context)
                .load(it.profilePicture)
                .error(R.drawable.account_circle_24)
                .circleCrop()
                .into(binding.imgProfile)

            oldBio = it.bio
            binding.edtBio.setText(it.bio)
        }

        editProfileViewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            GlideApp.with(binding.imgProfile.context)
                .load(uri)
                .error(R.drawable.account_circle_24)
                .circleCrop()
                .into(binding.imgProfile)
        }

        editProfileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSave.text = if (isLoading) "" else "Save"
            binding.btnSave.background.alpha = if (isLoading) 77 else 255
        }

        editProfileViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                showErrorDialog(error)
            }
        }

        editProfileViewModel.message.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                newBio?.let { selfProfileViewModel.setProfileBio(it) }

                showSuccessDialog(message)
            }
        }
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            showDiscardDialog()
        }

        binding.edtImgContainer.setOnClickListener {
            startGallery()
        }

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                if (newBio == oldBio) newBio = null

                editProfileViewModel.updateSelfProfile(newBio)
            }
        }
    }

    private fun setupTextChangedListeners() {
        binding.edtBio.addTextChangedListener { text ->
            newBio = text.toString()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showErrorDialog(error: String) {
        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle(error)
            .setPositiveButton("Try Again") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showSuccessDialog(message: MessageResponse) {
        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle(message.message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                navController.navigateUp()
            }
            .show()
    }

    private fun showDiscardDialog() {
        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle("Discard Changes")
            .setMessage("Are you sure you want to discard changes?")
            .setPositiveButton("Yes") { _, _ ->
                navController.navigateUp()
            }
            .setNegativeButton("No", null)
            .show()
    }
}