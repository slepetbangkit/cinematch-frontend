package com.slepetbangkit.cinematch.view.register

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.databinding.ActivityRegisterBinding
import com.slepetbangkit.cinematch.view.custom.EditEmailView
import com.slepetbangkit.cinematch.view.custom.EditPasswordView
import com.slepetbangkit.cinematch.view.custom.EditUsernameView

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    private var isUsernameValid = false
    private var isEmailValid = false
    private var isPasswordValid = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupValidationListeners()
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
        binding.registerBtn.isClickable = isAllValid
        binding.registerBtn.background.alpha = if (isAllValid) 255 else 77
    }
}