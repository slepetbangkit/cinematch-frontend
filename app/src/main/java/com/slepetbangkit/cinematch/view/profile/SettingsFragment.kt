package com.slepetbangkit.cinematch.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentProfileBinding
import com.slepetbangkit.cinematch.databinding.FragmentSearchBinding
import com.slepetbangkit.cinematch.databinding.FragmentSettingsBinding
import com.slepetbangkit.cinematch.helpers.ViewModelFactory
import com.slepetbangkit.cinematch.view.welcome.WelcomeActivity

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        profileViewModel = ViewModelFactory.getInstance(sessionRepository).create(ProfileViewModel::class.java)
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.aboutUsBtn.setOnClickListener {
            navController.navigate(R.id.action_navigation_settings_to_navigation_about_us)
        }

        binding.logOutBtn.setOnClickListener {
            AlertDialog.Builder(requireContext(), R.style.AlertDialog)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    profileViewModel.logout()
                    val intent = Intent(activity, WelcomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}