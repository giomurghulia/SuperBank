package com.example.superbank.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                            findNavController().navigate(HomeFragmentDirections.actionGlobalTransferFragment())
                        }
                        HomeActionEnum.OFFER -> {
                            findNavController().navigate(HomeFragmentDirections.actionGlobalOffersFragment())
                        }
                        HomeActionEnum.CURRENCY -> {
                        }
                        HomeActionEnum.ALL_CARD -> {
                            findNavController().navigate(HomeFragmentDirections.actionGlobalCardFragment())
                        }
                        HomeActionEnum.ALL_TRANSACTION -> {
                            findNavController().navigate(HomeFragmentDirections.actionGlobalTransactionFragment())
                        }
                    }
                }
            }
        }

        adapter.setCallBack(object : MainAdapter.CallBack {
            override fun onItemClick(action: HomeActionEnum) {
                viewModel.homeAction(action)
            }
        })
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = adapter

        binding.avatarImage.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalProfileFragment())
        }
    }

}