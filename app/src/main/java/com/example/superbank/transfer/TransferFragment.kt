package com.example.superbank.transfer

import android.annotation.SuppressLint
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
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.superbank.cards.CardsPagerAdapter
import com.example.superbank.databinding.FragmentTransferBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class TransferFragment : Fragment() {
    private lateinit var binding: FragmentTransferBinding
    private val viewModel: TransferViewModel by viewModels()

    private val cardsAdapter = CardsPagerAdapter()

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
        TabLayoutMediator(binding.tabLayout, binding.cardViewpager) { tab, position ->
            //Some implementation
        }.attach()


        binding.addressInput.doAfterTextChanged {
            val address = it.toString()

            viewModel.getFields(address)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fields.collect {
                    when (it) {
                        is Resource.Success -> {
                            binding.addressFullNameText.text = it.user.fullName
                        }
                        is Resource.Error -> {
                            binding.addressFullNameText.text = it.errorMessage
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

}
