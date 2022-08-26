package com.example.superbank.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.superbank.SharedViewModel
import com.example.superbank.authorized.AuthorizedUserViewModel
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentGuestUserBinding
import com.example.superbank.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : BaseFragment<FragmentProfileBinding>(
    FragmentProfileBinding::inflate
) {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logOutButton.setOnClickListener {
            Firebase.auth.signOut()
            sharedViewModel.logOutUser()
        }
    }
}