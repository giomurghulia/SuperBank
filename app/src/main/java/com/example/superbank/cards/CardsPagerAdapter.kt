package com.example.superbank.cards

import android.annotation.SuppressLint
import android.text.TextUtils.substring
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superbank.R
import com.example.superbank.databinding.LayoutCardItemBinding

class CardsPagerAdapter : RecyclerView.Adapter<CardsPagerAdapter.ViewHolder>() {

    private var cards = emptyList<Card>()

    private val VISA = "VISA"
    private val MASTERCARD = "Mastercard"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardsPagerAdapter.ViewHolder {
        return ViewHolder(
            LayoutCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardsPagerAdapter.ViewHolder, position: Int) {
        val item = cards[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return cards.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(cards: List<Card>) {
        this.cards = cards
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: LayoutCardItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Card) {
            binding.carNumberText.text = getCardNumber(item.cardNumber)
            binding.balanceText.text = item.cardBallance.toString()
            binding.cardDateText.text = item.cardDate

            if (item.cardType == VISA) {
                binding.cardTypeImage.setImageResource(R.drawable.ic_visa)
            }
            if (item.cardType == MASTERCARD){
                binding.cardTypeImage.setImageResource(R.drawable.ic_mastercard)
            }
        }
    }

    private fun getCardNumber(cardNum: String): String {
        return "**** **** **** " + cardNum.substring(cardNum.length - 5, cardNum.length - 1)
    }
}