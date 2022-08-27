package com.example.superbank.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentProfileBinding
import com.example.superbank.extensions.load
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment<FragmentProfileBinding>(
    FragmentProfileBinding::inflate
) {
    private fun listeners() {
        binding.logOutButton.setOnClickListener {
            Firebase.auth.signOut()
            sharedViewModel.logOutUser()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.authorizedUserData.collect { user ->
                    user?.let {
                        with(binding) {
                            avatar.load(it.avatar)
                            email.text = user.email
                            fullName.text = user.fullName
                            phone.text = user.mobile
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listeners()
        observeData()

    }
}