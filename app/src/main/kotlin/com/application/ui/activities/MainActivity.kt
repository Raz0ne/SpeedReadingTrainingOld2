package com.application.ui.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.iterator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.application.R
import com.application.databinding.ActivityMainBinding
import com.application.ui.fragments.navigation.adapters.TextFormatter

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _: NavController?,
                                                        navDestination: NavDestination,
                                                        _: Bundle? ->
            when (navDestination.id) {
                R.id.trainingFragment,
                R.id.settingsFragment,
                R.id.colorSchemeFragment,
                R.id.colorSchemeCustomFragment,
                R.id.fontSettingsFragment,
                R.id.accountFragment ->
                    showBottomNav()

                else -> hideBottomNav()
            }

            when (navDestination.id) {
                R.id.trainingFragment,
                R.id.settingsFragment,
                R.id.colorSchemeFragment,
                R.id.colorSchemeCustomFragment,
                R.id.fontSettingsFragment,
                R.id.accountFragment,
                R.id.emailResettingFragment,
                R.id.passwordResettingFragment ->
                    showToolbar()

                else -> hideToolbar()
            }
        }

        bottomNavView = binding.bottomNavigation

        setSupportActionBar(binding.toolbar)
        with(AppBarConfiguration(bottomNavView.menu)) {
            binding.toolbar.setupWithNavController(navController, this)
        }

        bottomNavView.setupWithNavController(navController)
        bottomNavView.setOnItemReselectedListener { item ->
            navController.popBackStack(item.itemId, false)
        }
        bottomNavView.setOnItemSelectedListener { item ->
            onNavDestinationSelected(item, navController)
            true
        }

        TextFormatter.sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser?.reload()
        if (currentUser == null)
            navController.navigate(R.id.loginFragment)
    }

    private fun showBottomNav() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigation.visibility = View.GONE
    }

    private fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
        title = navController.currentDestination!!.label
    }

    private fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
    }

    fun checkPermission(permission: String) : Boolean =
        if (ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1)

            ContextCompat.checkSelfPermission(this, permission) ==
                    PackageManager.PERMISSION_GRANTED
        } else
            true
}