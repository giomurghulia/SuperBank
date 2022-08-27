package com.example.superbank.nointernetconection

import android.content.Intent
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentNoInternetBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class NoInternetFragment :
    BaseFragment<FragmentNoInternetBinding>(FragmentNoInternetBinding::inflate) {
    override fun init() {
        if (isOnline()) {
            restart()
        }
    }

    override fun listeners() {
        binding.tryAgain.setOnClickListener {
            restart()
        }
        binding.logOutButton.setOnClickListener {
            Firebase.auth.signOut()
            sharedViewModel.logOutUser()
        }
    }
    private fun restart(){
        val intent = requireActivity().intent
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_NO_ANIMATION
        )
        requireActivity().overridePendingTransition(0, 0)
        requireActivity().finish()

        requireActivity().overridePendingTransition(0, 0)
        startActivity(intent)
    }


}