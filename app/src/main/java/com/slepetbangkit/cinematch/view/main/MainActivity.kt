package com.slepetbangkit.cinematch.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.local.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.databinding.ActivityMainBinding
import com.slepetbangkit.cinematch.view.profile.ProfileViewModel
import com.slepetbangkit.cinematch.view.welcome.WelcomeActivity
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


        lifecycleScope.launch {
            val accessTokenFlow = sessionPreferences.getAccessToken()
            val refreshTokenFlow = sessionPreferences.getRefreshToken()

            combine(accessTokenFlow, refreshTokenFlow) { accessToken, refreshToken ->
                Pair(accessToken, refreshToken)
            }.collect { (accessToken, refreshToken) ->
                if (accessToken.isBlank() || refreshToken.isBlank()) {
                    val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    setupNavBar()
                }
            }
        }

    }
    private fun setupNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navController)
    }
}