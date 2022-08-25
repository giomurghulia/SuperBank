package com.example.superbank.guest

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.superbank.databinding.FragmentGuestUserBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GuestUserFragment : Fragment() {

    private lateinit var binding: FragmentGuestUserBinding

    private val viewModel: GuestUserViewModel by viewModels()

    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentGuestUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateErrorStates()

        binding.logInButton.setOnClickListener {
            if (submitForm()) {
                val email = binding.emailInput.text.toString()
                val password = binding.passInput.text.toString()

                logIn(email, password)
            }
        }

        binding.registerButton.setOnClickListener {
            if (submitForm()) {
                val email = binding.emailInput.text.toString()
                val password = binding.passInput.text.toString()

                register(email, password)
            }
        }
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")

                    Toast.makeText(
                        context, "createUserWithEmail:success",
                        Toast.LENGTH_SHORT
                    ).show()

                    logIn(email, password)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser

                    Toast.makeText(
                        context, "signInWithEmail:success",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(
                        GuestUserFragmentDirections.actionGuestUserFragmentToAuthorizedUserFragment()
                    )

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun submitForm(): Boolean = with(binding) {
        val email = emailInput.text?.toString()
        val password = passInput.text?.toString()

        var isFormValid = true

        if (!isValidEmail(email)) {
            emailLayout.error = ("Incorrect Email")
            isFormValid = false
        }

        if (!(password != null && password.isNotBlank() && password.length > 6)) {
            passLayout.error = ("Incorrect Password")
            isFormValid = false
        }

        return isFormValid
    }

    private fun isValidEmail(email: String?): Boolean {
        return if (email.isNullOrBlank()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    private fun updateErrorStates() = with(binding) {
        emailInput.doAfterTextChanged {
            emailLayout.error = null
        }
        passInput.doAfterTextChanged {
            passLayout.error = null
        }
    }
}