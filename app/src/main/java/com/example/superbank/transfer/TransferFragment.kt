package com.example.superbank.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.superbank.databinding.FragmentTransferBinding
import kotlinx.coroutines.launch

class TransferFragment : Fragment() {
    private lateinit var binding: FragmentTransferBinding
    private val viewModel: TransferViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateErrorStates()

        binding.addressInput.doAfterTextChanged {
            binding.addressFullNameText.text = ""
            val address = it.toString()

            viewModel.getFields(address)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fields.collect { it ->
                    binding.loaderProgressBar.visibility = View.GONE
                    when (it) {
                        is Resource.Success -> {
                            binding.addressFullNameText.text = it.user.fullName
                        }
                        is Resource.Error -> {
                            binding.addressLayout.error = "Incorrect Address"
                        }
                        is Resource.Loading -> {
                            binding.loaderProgressBar.visibility = View.VISIBLE
                        }
                    }

                }
            }
        }

    }

    private fun updateErrorStates() = with(binding) {
        addressInput.doAfterTextChanged {
            addressLayout.error = null
        }
        amountInput.doAfterTextChanged {
            amountLayout.error = null
        }
    }

}
