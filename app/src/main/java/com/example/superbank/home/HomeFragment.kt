package com.example.superbank.home

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.superbank.R
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentHomeBinding
import com.example.superbank.transactions.adapters.models.CardType
import com.example.superbank.transactions.adapters.models.InnerModel
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    private val VISA = "VISA"
    private val MASTERCARD = "Mastercard"

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = MainAdapter()


    @SuppressLint("SetTextI18n")
    override fun init() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.light_blue)

        sharedViewModel.checkAuthorizedUser()
        viewModel.getTransactionsAndCard()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.authorizedUserData.collect {
                    binding.fullNameText.text = it?.fullName

                    Glide
                        .with(binding.root.context)
                        .load(it?.avatar)
                        .centerInside()
                        .circleCrop()
                        .into(binding.avatarImage)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listItems.collect {
                    adapter.submitList(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.action.collect {
                    when (it) {
                        HomeActionEnum.TRANSFER -> {
                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTransferFragment())
                        }
                        HomeActionEnum.OFFER -> {
                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToOffersFragment())
                        }
                        HomeActionEnum.CURRENCY -> {
                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCurrencyFragment())
                        }
                        else -> {}
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.balance.collect {
                    binding.amountText.text = "$$it"
                }
            }
        }

        adapter.setCallBack(object : MainAdapter.CallBack {
            override fun onItemClick(action: HomeActionEnum) {
                sharedViewModel.homeAction(action)
                viewModel.homeAction(action)
            }

            override fun onTransactionClick(item: HomeListItem.TransactionsItem) {
                val innerItem = InnerModel(
                    item.title,
                    item.type,
                    item.amount,
                    item.cardLastDigits,
                    item.description,
                    item.cardType
                )
                findNavController().navigate(
                    HomeFragmentDirections.actionGlobalTransactionInfoFragment(innerItem)
                )
            }
        })
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = adapter

        binding.avatarImage.setOnClickListener {
            sharedViewModel.homeAction(HomeActionEnum.PROFILE)
        }
    }

}