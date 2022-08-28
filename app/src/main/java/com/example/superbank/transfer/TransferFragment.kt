package com.example.superbank.transfer

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.cards.CardsPagerAdapter
import com.example.superbank.databinding.FragmentTransferBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class TransferFragment : BaseFragment<FragmentTransferBinding>(
    FragmentTransferBinding::inflate
) {
    private val viewModel: TransferViewModel by viewModels()

    private val cardsAdapter = CardsPagerAdapter()


    override fun init() {
        updateErrorStates()
        updateQuickAmount()

        viewModel.getCards()

        binding.cardViewpager.adapter = cardsAdapter
        binding.cardViewpager.setPageTransformer(MarginPageTransformer(40))
        binding.cardViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                viewModel.onCardSelected(position)
            }
        })
        TabLayoutMediator(binding.tabLayout, binding.cardViewpager) { _, _ ->
            //Some implementation
        }.attach()


        binding.addressInput.doAfterTextChanged {
            val address = it.toString()

            viewModel.getFields(address)
        }

        binding.sendButton.setOnClickListener {
            if (checkForm()) {
                val address = binding.addressInput.text.toString()
                val amount = binding.amountInput.text.toString().toDouble()
                viewModel.makeTransfer(address, amount)
            }
        }

        binding.backImage.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transfer.collect {
                    when (it) {
                        is Resource.Success -> {
                            binding.addressFullNameText.text = it.user.fullName

                            clocheKeyboard()
                        }
                        is Resource.Error -> {
                            binding.addressFullNameText.text = it.errorMessage

                            clocheKeyboard()
                        }
                        is Resource.SuccessTransfer -> {
                            successTransfer()
                        }

                        is Resource.ErrorTransfer -> {
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Super Bank")
                            builder.setMessage(it.errorMessage)
                            val newAlert = builder.create()
                            newAlert.show()
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cards.collect {
                    cardsAdapter.submitList(it)
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

    @SuppressLint("SetTextI18n")
    private fun updateQuickAmount() = with(binding) {
        button5Text.setOnClickListener {
            binding.amountInput.setText("5")
        }
        button10Text.setOnClickListener {
            binding.amountInput.setText("10")
        }
        button20Text.setOnClickListener {
            binding.amountInput.setText("20")
        }
        button50Text.setOnClickListener {
            binding.amountInput.setText("50")
        }
    }

    private fun checkForm(): Boolean {
        val address = binding.addressInput.text?.toString()
        val amount = binding.amountInput.text?.toString()

        var checker = true

        if (address.isNullOrEmpty()) {
            binding.addressLayout.error = "Address Is Empty"
            checker = false
        }

        if (amount.toString().isBlank()) {
            binding.amountLayout.error = "Fill Amount"
            checker = false
        }

        return checker
    }

    private fun successTransfer() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Super Bank")
        builder.setMessage("Transaction Success")
        val newAlert = builder.create()
        newAlert.show()

        binding.addressInput.setText("")
        binding.amountInput.setText("")
    }

    private fun clocheKeyboard() {
        val controller = WindowCompat.getInsetsController(requireActivity().window, binding.root)
        controller.hide(WindowInsetsCompat.Type.ime())
    }
}
