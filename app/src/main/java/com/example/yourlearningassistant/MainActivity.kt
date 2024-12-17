package com.example.yourlearningassistant

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.yourlearningassistant.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.lecturesFragment -> {
                    // Always navigate to lectures list, clearing the back stack
                    navController.navigate(R.id.lecturesFragment, null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.nav_graph, false)
                            .build()
                    )
                    true
                }
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                else -> false
            }
        }

        // Update toolbar title based on destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    supportActionBar?.apply {
                        title = "HOME"
                        setDisplayHomeAsUpEnabled(false)
                    }
                }
                R.id.lecturesFragment -> {
                    supportActionBar?.apply {
                        title = "LECTURES"
                        setDisplayHomeAsUpEnabled(false)
                    }
                }
                R.id.notesFragment -> {
                    supportActionBar?.apply {
                        title = "NOTES"
                        setDisplayHomeAsUpEnabled(true)
                        setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
                    }
                }
            }
        }

        // Handle back button clicks
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}