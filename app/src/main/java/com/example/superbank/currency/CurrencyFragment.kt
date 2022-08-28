package com.example.superbank.currency

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.superbank.R
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentCurrencyBinding
import kotlinx.coroutines.launch

class CurrencyFragment : BaseFragment<FragmentCurrencyBinding>(
    FragmentCurrencyBinding::inflate
) {
    private val viewModel: CurrencyViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("ResourceType")
    override fun init() {
        val currencyName = resources.getStringArray(R.array.currency_name)

        viewModel.getCurrency(currencyName)

        binding.firstInput.setText("0")


        val firstPicker = binding.firstPicker
        val secondPicker = binding.secondPicker


        firstPicker.minValue = 0
        secondPicker.minValue = 0

        firstPicker.maxValue = 3
        secondPicker.maxValue = 3

        firstPicker.displayedValues = currencyName
        secondPicker.displayedValues = currencyName

        binding.firstInput.doAfterTextChanged {
            val amount = binding.firstInput.text.toString().toDoubleOrNull() ?: 0.0

            val firsValue = firstPicker.value
            val secondValue = secondPicker.value

            viewModel.setDate(firsValue, secondValue, amount)
        }

        firstPicker.setOnScrollListener { numberPicker, i ->
            val amount = binding.firstInput.text.toString().toDoubleOrNull() ?: 0.0
            val firsValue = firstPicker.value
            val secondValue = secondPicker.value

            viewModel.setDate(firsValue, secondValue, amount)
        }


        secondPicker.setOnScrollListener { numberPicker, i ->
            val amount = binding.firstInput.text.toString().toDoubleOrNull() ?: 0.0
            val firsValue = firstPicker.value
            val secondValue = secondPicker.value

            viewModel.setDate(firsValue, secondValue, amount)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collect {
                    binding.secondInput.setText(it.toString())
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ratesState.collect {
                    binding.loaderProgressBar.visibility = View.GONE
                    when (it) {
                        is Resource.Success -> {
                            Toast.makeText(
                                context,
                                "Success Get Currency Rates",
                                Toast.LENGTH_SHORT
                            ).show()

                            val amount = binding.firstInput.text.toString().toDouble()
                            val firsValue = firstPicker.value
                            val secondValue = secondPicker.value

                            viewModel.setDate(firsValue, secondValue, amount)

                        }
                        is Resource.Error -> {
                            Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            binding.loaderProgressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        binding.backImage.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}
