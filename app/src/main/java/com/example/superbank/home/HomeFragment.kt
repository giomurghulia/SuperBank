package com.example.superbank.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.superbank.SharedViewModel
import com.example.superbank.authorized.AuthorizedUserViewModel
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.checkAuthorizedUser()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.authorizedUserData.collect {
                    binding.a.text = it?.fullName
                    binding.b.text = it?.email

                    Glide
                        .with(binding.root.context)
                        .load(it?.avatar)
                        .centerCrop()
                        .into(binding.c);
                }
            }
        }
    }

}