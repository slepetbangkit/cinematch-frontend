package com.slepetbangkit.cinematch.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.ActivityLoginBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.AuthViewModelFactory
import com.slepetbangkit.cinematch.view.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionRepository: SessionRepository
    private lateinit var factory: AuthViewModelFactory
    private lateinit var loginViewModel: LoginViewModel

    private var isUsernameValid = false
    private var isPasswordValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        sessionRepository = Injection.provideSessionRepository(this)
        factory = AuthViewModelFactory.getInstance(sessionRepository)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        setupValidationListeners()

        loginViewModel.loginResult.observe(this) { loginResponse ->
            lifecycleScope.launch {
                loginResponse.access.let { sessionRepository.saveAccessToken(it) }
                loginResponse.refresh.let { sessionRepository.saveRefreshToken(it) }
                loginResponse.access.let { sessionRepository.saveUsername(it) }

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