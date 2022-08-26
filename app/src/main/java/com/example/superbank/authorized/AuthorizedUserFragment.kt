package com.example.superbank.authorized

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.superbank.AuthorizedUser
import com.example.superbank.R
import com.example.superbank.databinding.FragmentAuthorizedUserBinding
import com.example.superbank.networking.RetrofitClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AuthorizedUserFragment : Fragment() {

    private lateinit var binding: FragmentAuthorizedUserBinding

    private val viewModel: AuthorizedUserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentAuthorizedUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.logInUser(AuthorizedUser("","","","",""))

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.authorized_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupWithNavController(binding.bottomNavigationView, navController)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authorizedUserData.collect{
                    if (it == null){
                        logOut()
                    }
                }
            }
        }
    }
    private fun logOut(){
        findNavController().navigate(AuthorizedUserFragmentDirections.actionAuthorizedUserFragmentToGuestUserFragment())
    }
}