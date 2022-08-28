package com.example.superbank.transactions.transactioninfo


import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentTransactionInfoBinding
import com.example.superbank.transactions.COVERED_PART_OR_CARD

class TransactionInfoFragment :
    BaseFragment<FragmentTransactionInfoBinding>(FragmentTransactionInfoBinding::inflate) {
    private val args: TransactionInfoFragmentArgs by navArgs()


    override fun init() {
        with(binding) {
            val card = "$COVERED_PART_OR_CARD${args.cardLastNumbers}"
            carNumberText.text = card
            title.text = args.title
            type.text = args.type
            val amountString =
                if (args.amount[0] == '-') "-$${args.amount.subSequence(1..args.amount.lastIndex)}" else "$${args.amount}"
            amount.text = amountString
//            date.text = args.date
            description.text = args.description
            backImage.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }
}