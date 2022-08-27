package com.example.superbank.authorized

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.superbank.AuthorizedUser
import com.example.superbank.R
import com.example.superbank.SharedViewModel
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentAuthorizedUserBinding
import com.example.superbank.databinding.FragmentTransactionsBinding
import com.example.superbank.networking.RetrofitClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AuthorizedUserFragment : BaseFragment<FragmentAuthorizedUserBinding>(
    FragmentAuthorizedUserBinding::inflate
) {
    private val viewModel: AuthorizedUserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.authorized_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.offersFragment -> {binding.bottomNavigationView.visibility = View.GONE}
                R.id.transferFragment -> {binding.bottomNavigationView.visibility = View.GONE}
                R.id.transactionInfoFragment -> {binding.bottomNavigationView.visibility = View.GONE}
                else -> { binding.bottomNavigationView.visibility = View.VISIBLE }
            }
        }

        setupWithNavController(binding.bottomNavigationView, navController)

        sharedViewModel.getAuthorizedUserDate()


    }
}