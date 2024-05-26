package com.slepetbangkit.cinematch.view.login

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
import com.slepetbangkit.cinematch.databinding.ActivityLoginBinding
import com.slepetbangkit.cinematch.view.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionPrefs: SessionPreferences
    private val loginViewModel: LoginViewModel by viewModels()

    private var isUsernameValid = false
    private var isPasswordValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        sessionPrefs = SessionPreferences.getInstance(this.dataStore)
        setContentView(binding.root)

        setupValidationListeners()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        loginViewModel.loginResult.observe(this) { loginResponse ->
            lifecycleScope.launch {
                loginResponse.access?.let { sessionPrefs.saveAccessToken(it) }
                loginResponse.refresh?.let { sessionPrefs.saveRefreshToken(it) }

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.text = if (isLoading) "" else "Login"
            binding.btnLogin.background.alpha = if (isLoading) 77 else 255
        }

        loginViewModel.error.observe(this) { error ->
            if (error != null) {
                AlertDialog.Builder(this, R.style.AlertDialog)
                    .setTitle(error)
                    .setPositiveButton("Try Again") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun setupValidationListeners() {
        updateButtonState()
        binding.edtUname.onValidationChanged = { isValid ->
            isUsernameValid = isValid
            updateButtonState()
        }
        binding.edtPassword.onValidationChanged = { isValid ->
            isPasswordValid = isValid
            updateButtonState()
        }
    }

    private fun updateButtonState() {
        val isAllValid = isUsernameValid && isPasswordValid
        binding.btnLogin.isClickable = isAllValid
        binding.btnLogin.background.alpha = if (isAllValid) 255 else 77
    }

    private fun handleLogin() {
        val username = binding.edtUname.text.toString()
        val password = binding.edtPassword.text.toString()
        loginViewModel.login(username, password)
    }
}