package com.example.superbank.transactions.transactioninfo


import android.content.res.ColorStateList
import androidx.navigation.fragment.navArgs
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentTransactionInfoBinding
import com.example.superbank.transactions.COVERED_PART_OR_CARD

class TransactionInfoFragment :
    BaseFragment<FragmentTransactionInfoBinding>(FragmentTransactionInfoBinding::inflate) {
    private val args: TransactionInfoFragmentArgs by navArgs()


    override fun init() {
        val transaction = args.transaction
        with(binding) {
            val card = "$COVERED_PART_OR_CARD${transaction.cardLastDigits}"
            carNumberText.text = card
            title.text = transaction.title
            type.text = transaction.type.toString()
            val amountString = transaction.amount.toString()

            val amountStringClear =
                if (amountString[0] == '-') "-$${amountString.subSequence(1..amountString.lastIndex)}" else "$${amountString}"
            amount.text = amountStringClear
            description.text = transaction.description
            cardTypeImage.setImageResource(transaction.cardType.icon)
            iconImage.setImageResource(transaction.type.icon())
            titleText.text = transaction.cardType.toString()
            cardTypeImage.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(transaction.type.backgroundTint))
            backImage.setOnClickListener {
                requireActivity().onBackPressed()
            }

        }
    }
}