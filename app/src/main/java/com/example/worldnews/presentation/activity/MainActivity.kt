package com.example.worldnews.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.worldnews.R
import com.example.worldnews.databinding.ActivityMainBinding
import com.example.worldnews.presentation.extension.navigateSafe
import com.example.worldnews.presentation.navigation.NavManager
import com.example.worldnews.presentation.util.NotificationWorkManager
import com.example.worldnews.presentation.util.WORK_TAG
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
open class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val binding: ActivityMainBinding by viewBinding()

    @Inject
    lateinit var navManager: NavManager

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        initNavController()
        initActionBar()
        initBottomNavigation()
        initNavManager()
        initNotificationWorkManager()
    }

    private fun initNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
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

    //

    private fun initNotificationWorkManager() {
        /**
         * Notification message every day
         */
        val notificationWork = PeriodicWorkRequestBuilder<NotificationWorkManager>(
            20, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWork
        )
    }

    fun getNavController(): NavController = navController

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}