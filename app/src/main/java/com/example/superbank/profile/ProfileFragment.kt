package com.example.superbank.profile

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
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

    override fun init() {
        Firebase.auth.currentUser?.reload()
        val user = Firebase.auth.currentUser

        listeners()
        bindObservers()
        updateErrorStates()
        binding.hideEmail.setOnClickListener {
            binding.emailChangeFrame.visibility = View.GONE
            binding.emailChangeInput.setText("")
        }
        binding.hidePassword.setOnClickListener {
            binding.passwordChangeFrame.visibility = View.GONE
            binding.passChangeInput.setText("")
        }
        binding.changeEmail.setOnClickListener {
            val isVisible = binding.emailChangeFrame.visibility == View.VISIBLE

            binding.emailChangeFrame.visibility = View.VISIBLE
            binding.changeEmail.hint = "Update Email"

            val email = binding.emailChangeInput.text?.toString()

            if (isVisible) {
                if (!isValidEmail(email)) {
                    binding.emailChangeLayout.error = ("Incorrect Email")
                } else {
                    user!!.updateEmail(email!!)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                sharedViewModel.getAuthorizedUserDate()
                                Log.d(TAG, "User email address updated.")
                                Toast.makeText(
                                    context,
                                    "User Email address updated.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                                clearAndCloseInput()

                            } else {
                                Toast.makeText(
                                    context,
                                    "Cant change Email, log out and try again",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                }
            }
        }

        binding.changePassword.setOnClickListener {
            val isVisible = binding.passwordChangeFrame.visibility == View.VISIBLE
            binding.passwordChangeFrame.visibility = View.VISIBLE
            binding.changePassword.hint = "Update Password"

            val password = binding.passChangeInput.text?.toString()

            if (isVisible) {
                if (!(password != null && password.isNotBlank() && password.length > 6)) {
                    binding.passChangeLayout.error = ("Incorrect Password")
                } else {
                    user!!.updatePassword(password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User password updated.")
                                Toast.makeText(
                                    context,
                                    "User Password updated.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                                clearAndCloseInput()

                            } else {
                                Toast.makeText(
                                    context,
                                    "Cant change Password, log out and try again",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                }
            }

        }
    }

    private fun clearAndCloseInput() {
        binding.passChangeInput.setText("")
        binding.emailChangeInput.setText("")

        binding.emailChangeFrame.visibility = View.GONE
        binding.changePassword.hint = "Change Password"

        binding.passwordChangeFrame.visibility = View.GONE
        binding.changeEmail.hint = "Change Email"

    }

    private fun isValidEmail(email: String?): Boolean {
        return if (email.isNullOrBlank()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    private fun updateErrorStates() = with(binding) {
        emailChangeInput.doAfterTextChanged {
            emailChangeLayout.error = null
        }
        passChangeInput.doAfterTextChanged {
            passChangeLayout.error = null
        }
    }

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