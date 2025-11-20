package com.edunova

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.edunova.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupSystemWindowInsets()
        setupNavigation()
    }
    
    private fun setupSystemWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Add top margin for status bar + extra spacing for better UI
            v.setPadding(0, systemBars.top + 16, 0, 0)
            insets
        }
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        
        // Setup bottom navigation with NavController
        binding.bottomNavigation.setupWithNavController(navController)

		// Hide bottom nav on auth/splash screens
		navController.addOnDestinationChangedListener { _, destination, _ ->
			val hide = when (destination.id) {
				R.id.nav_splash, R.id.nav_login, R.id.nav_register -> true
				else -> false
			}
			binding.bottomNavigation.isVisible = !hide
		}
    }
}