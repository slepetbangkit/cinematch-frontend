package com.slepetbangkit.cinematch.view.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.slepetbangkit.cinematch.data.local.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.databinding.ActivityMainBinding
import com.slepetbangkit.cinematch.view.login.LoginActivity
import com.slepetbangkit.cinematch.view.profile.ProfileActivity
import com.slepetbangkit.cinematch.view.register.RegisterActivity
import com.slepetbangkit.cinematch.view.search.SearchActivity
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionPreferences: SessionPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sessionPreferences = SessionPreferences.getInstance(this.dataStore)
        setContentView(binding.root)

        binding.btnProfile.setOnClickListener {
            val profileIntent = Intent(this, ProfileActivity::class.java)
            startActivity(profileIntent)
        }

        binding.btnSearch.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        binding.btnLogin.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        binding.btnRegister.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                sessionPreferences.clear()
            }
        }

        lifecycleScope.launch {
            val accessTokenFlow = sessionPreferences.getAccessToken()
            val refreshTokenFlow = sessionPreferences.getRefreshToken()

            combine(accessTokenFlow, refreshTokenFlow) { accessToken, refreshToken ->
                Pair(accessToken, refreshToken)
            }.collect { (accessToken, refreshToken) ->
                if (accessToken.isEmpty() || refreshToken.isEmpty()) {
                    binding.btnLogin.isClickable = true
                    binding.btnRegister.isClickable = true
                    binding.btnLogout.isClickable = false
                    binding.btnProfile.isClickable = false
                    binding.btnSearch.isClickable = false
                    binding.btnLogin.background.alpha = 255
                    binding.btnRegister.background.alpha = 255
                    binding.btnLogout.background.alpha = 77
                    binding.btnProfile.background.alpha = 77
                    binding.btnSearch.background.alpha = 77

                    binding.tvSessionTokenValue.text = "Null"
                    binding.tvRefreshTokenValue.text = "Null"
                } else {
                    binding.btnLogin.isClickable = false
                    binding.btnRegister.isClickable = false
                    binding.btnLogout.isClickable = true
                    binding.btnProfile.isClickable = true
                    binding.btnSearch.isClickable = true
                    binding.btnLogin.background.alpha = 77
                    binding.btnRegister.background.alpha = 77
                    binding.btnLogout.background.alpha = 255
                    binding.btnProfile.background.alpha = 255
                    binding.btnSearch.background.alpha = 255

                    binding.tvSessionTokenValue.text = accessToken
                    binding.tvRefreshTokenValue.text = refreshToken
                }
            }
        }
    }
}