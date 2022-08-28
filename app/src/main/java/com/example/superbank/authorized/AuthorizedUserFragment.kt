package com.example.superbank.authorized


import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.superbank.R
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentAuthorizedUserBinding
import com.example.superbank.home.HomeActionEnum
import kotlinx.coroutines.launch


class AuthorizedUserFragment : BaseFragment<FragmentAuthorizedUserBinding>(
    FragmentAuthorizedUserBinding::inflate
) {
    override fun init() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.authorized_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.offersFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.transferFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.transactionInfoFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.currencyFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

        setupWithNavController(binding.bottomNavigationView, navController)

        sharedViewModel.getAuthorizedUserDate()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.action.collect {
                    when (it) {
                        HomeActionEnum.ALL_CARD -> {
                            binding.bottomNavigationView.selectedItemId = R.id.CardFragment
                        }
                        HomeActionEnum.ALL_TRANSACTION -> {
                            binding.bottomNavigationView.selectedItemId = R.id.transactionFragment
                        }
                        HomeActionEnum.PROFILE -> {
                            binding.bottomNavigationView.selectedItemId = R.id.profileFragment
                        }
                        else -> {}

                    }
                }
            }
        }


    }
}