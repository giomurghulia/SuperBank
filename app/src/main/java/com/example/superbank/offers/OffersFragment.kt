package com.example.superbank.offers


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentOffersBinding
import com.example.superbank.offers.adapter.OfferModel
import com.example.superbank.offers.adapter.OffersAdapter

class OffersFragment : BaseFragment<FragmentOffersBinding>(FragmentOffersBinding::inflate) {
    private val offersAdapter by lazy {
        OffersAdapter()
    }

    private fun listeners() {
        binding.backImage.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun init() {
        offersAdapter.submitList(
            listOf(
                OfferModel(1,"offer1", "IDK", "https://images.unsplash.com/photo-1566275529824-cca6d008f3da?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NHx8cGhvdG98ZW58MHx8MHx8&w=1000&q=80","https://www.tbcbank.ge/web/de/web/guest/tbc-concept-new?fbclid=IwAR09vHiW6E3l2DXfOqO0p84CsGxLgc13FsAE6ToXQmVtnvcP6SxtrlpJLyw" ),
                OfferModel(2, "offer2", "IDK", "https://www.tbcbank.ge/web/documents/10184/640647/icr+eng+web.png/ee46f8fb-ed86-44db-b933-ff6eebe6e1fa?t=1661345367270", "https://stackoverflow.com/questions/53106203/firebase-unresolved-reference")

            )
        )
        with(binding.recycler) {
            adapter = offersAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listeners()
    }

}