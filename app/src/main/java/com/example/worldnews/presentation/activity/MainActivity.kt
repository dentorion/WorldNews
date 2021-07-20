package com.example.worldnews.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.worldnews.R
import com.example.worldnews.databinding.ActivityMainBinding
import com.example.worldnews.presentation.navigation.NavManager
import com.example.worldnews.presentation.extension.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class MainActivity: AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var navManager: NavManager

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavController()
        initActionBar()
        initBottomNavigation()
        initNavManager()
    }

    private fun initNavController() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun initActionBar() {
        setSupportActionBar(binding.myActionBar)

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.usFragment, R.id.plFragment, R.id.uaFragment, R.id.ruFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun initBottomNavigation() {
        binding.bottomNavigationMenu.setupWithNavController(navController)
    }

    private fun initNavManager() {
        navManager.setOnNavEvent {
            val currentFragment = navHostFragment.childFragmentManager.fragments[0]
            currentFragment?.navigateSafe(it)
        }
    }

    fun getNavController(): NavController = navController

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}