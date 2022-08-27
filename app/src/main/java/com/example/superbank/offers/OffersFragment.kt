package com.example.superbank.offers


import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentOffersBinding
import com.example.superbank.networking.responsestate.ResponseState
import com.example.superbank.offers.adapter.OfferModel
import com.example.superbank.offers.adapter.OffersAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OffersFragment : BaseFragment<FragmentOffersBinding>(FragmentOffersBinding::inflate) {
    private val offersAdapter by lazy {
        OffersAdapter()
    }
    private val viewModel: OffersViewModel by viewModels()

    private var list = listOf<OfferModel>()
    override fun listeners() {
        binding.backImage.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun init() {
        offersAdapter.submitList(
            list
        )
        with(binding.recycler) {
            adapter = offersAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        viewModel.getOffers()
    }

    override fun bindObservers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.requestState.collect {
                    when (it) {
                        is ResponseState.Success -> {
                            if (it.body.isNotEmpty()) {
                                list = it.body
                                offersAdapter.submitList(list)
                            }
                        }
                        is ResponseState.Error -> {
                            myToast(it.errorMessage)
                        }
                        is ResponseState.Load -> {}
                    }

                }
            }
        }
    }


}