package com.example.superbank.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.superbank.R
import com.example.superbank.SharedViewModel
import com.example.superbank.authorized.AuthorizedUserViewModel
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.cards.CardsPagerAdapter
import com.example.superbank.cards.QuickActionEnum
import com.example.superbank.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = MainAdapter()


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
                        .into(binding.avatarImage);
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
        })
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = adapter

        binding.avatarImage.setOnClickListener {
            sharedViewModel.homeAction(HomeActionEnum.PROFILE)
        }
    }

}