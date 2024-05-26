package com.slepetbangkit.cinematch.view.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.local.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.remote.response.RegisterResponse
import com.slepetbangkit.cinematch.databinding.ActivityRegisterBinding
import com.slepetbangkit.cinematch.view.main.MainActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sessionPrefs: SessionPreferences
    private val registerViewModel: RegisterViewModel by viewModels()

    private var isUsernameValid = false
    private var isEmailValid = false
    private var isPasswordValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        sessionPrefs = SessionPreferences.getInstance(this.dataStore)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnRegister.setOnClickListener { handleRegister() }
            setupValidationListeners()
        }
    }

    private fun setupValidationListeners() {
        updateButtonState()
        binding.edtUname.onValidationChanged = { isValid ->
            isUsernameValid = isValid
            updateButtonState()
        }
        binding.edtEmail.onValidationChanged = { isValid ->
            isEmailValid = isValid
            updateButtonState()
        }
        binding.edtPassword.onValidationChanged = { isValid ->
            isPasswordValid = isValid
            updateButtonState()
        }
    }

    private fun updateButtonState() {
        val isAllValid = isUsernameValid && isEmailValid && isPasswordValid
        binding.btnRegister.apply {
            isClickable = isAllValid
            background.alpha = if (isAllValid) 255 else 77
        }
    }

    private fun handleRegister() {
        val username = binding.edtUname.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        registerViewModel.register(username, email, password)
    }

    private fun observeViewModel() {
        registerViewModel.apply {
            registerResult.observe(this@RegisterActivity) { registerResponse ->
                handleRegisterResponse(registerResponse)
            }
            isLoading.observe(this@RegisterActivity) { isLoading ->
                updateLoadingState(isLoading)
            }
            error.observe(this@RegisterActivity) { error ->
                error?.let { showErrorDialog(it) }
            }
        }
    }

    private fun handleRegisterResponse(registerResponse: RegisterResponse) {
        lifecycleScope.launch {
            registerResponse.token?.access?.let { sessionPrefs.saveAccessToken(it) }
            registerResponse.token?.refresh?.let { sessionPrefs.saveRefreshToken(it) }
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnRegister.apply {
                text = if (isLoading) "" else "Register"
                background.alpha = if (isLoading) 77 else 255
            }
        }
    }

    private fun showErrorDialog(error: String) {
        AlertDialog.Builder(this, R.style.AlertDialog)
            .setTitle(error)
            .setPositiveButton("Try Again") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
