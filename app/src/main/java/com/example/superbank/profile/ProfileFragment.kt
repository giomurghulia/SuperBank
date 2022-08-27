package com.example.superbank.profile

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
    override fun listeners() {
        binding.logOutButton.setOnClickListener {
            Firebase.auth.signOut()
            sharedViewModel.logOutUser()
        }
    }

    override fun bindObservers() {
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

}