package com.slepetbangkit.cinematch.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.preferences.dataStore
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.ActivityMainBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.MainViewModelFactory
import com.slepetbangkit.cinematch.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionRepository: SessionRepository
    private lateinit var factory: MainViewModelFactory
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sessionRepository = Injection.provideSessionRepository(this)
        factory = MainViewModelFactory.getInstance(sessionRepository)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        setContentView(binding.root)

        mainViewModel.accessToken.observe(this) { accessToken ->
            if (accessToken.isBlank()) {
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                setupNavBar()
            }
        }

        // Handle the back button press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController

                // Check if we are not on the home screen
                if (navController.currentDestination?.id != R.id.nav_graph_home) {
                    // Navigate to the home screen
                    navController.popBackStack(R.id.nav_graph_home, false)
                } else {
                    // If already on home screen, finish the activity
                    finish()
                }
            }
        })
    }

    private fun setupNavBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_graph_home -> {
                    navController.navigate(R.id.nav_graph_home)
                    true
                }
                R.id.nav_graph_search -> {
                    navController.navigate(R.id.nav_graph_search)
                    true
                }
                R.id.nav_graph_activity -> {
                    navController.navigate(R.id.nav_graph_activity)
                    true
                }
                R.id.nav_graph_profile -> {
                    navController.navigate(R.id.nav_graph_profile)
                    true
                }
                else -> false
            }
        }
    }
}
