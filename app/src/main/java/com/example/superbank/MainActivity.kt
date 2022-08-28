package com.example.superbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment

import com.example.superbank.databinding.ActivityMainBinding
import com.example.superbank.guest.GuestUserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: SharedViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isNotLoaded.value && Firebase.auth.currentUser != null
            }
        }
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController


        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            navController.navigate(R.id.action_global_authorizedUserFragment)
        } else {
            navController.navigate(R.id.action_global_guestUserFragment)
        }

        this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.logOutUser.collect {
                    navController.navigate(R.id.action_global_guestUserFragment)
                }
            }
        }
        this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.noInternet.collect {
                    if (it == 1)
                        navController.navigate(R.id.action_global_noInternetFragment)
                }
            }
        }
    }

}
