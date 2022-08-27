package com.example.superbank.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.superbank.MainDiffUtil
import com.example.superbank.R
import com.example.superbank.databinding.LayoutCardTransactionBinding
import com.example.superbank.databinding.LayoutHomeCardItemBinding
import com.example.superbank.databinding.LayoutHomeQuickActionBinding
import com.example.superbank.databinding.LayoutItemHeaderBinding

class MainAdapter :
    ListAdapter<HomeListItem, RecyclerView.ViewHolder>(MainDiffUtil()) {
    private val VISA = "VISA"
    private val MASTERCARD = "Mastercard"

    private var callBack: CallBack? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            HomeListItem.ViewType.QUICK_ACTION.ordinal -> {
                QuickActionViewHolder(
                    LayoutHomeQuickActionBinding.inflate(layoutInflater, parent, false)
                )
            }
            HomeListItem.ViewType.ITEM_HEADER.ordinal -> {
                ItemHeaderViewHolder(
                    LayoutItemHeaderBinding.inflate(layoutInflater, parent, false)
                )
            }
            HomeListItem.ViewType.CARDS.ordinal -> {
                CardsItemViewHolder(
                    LayoutHomeCardItemBinding.inflate(layoutInflater, parent, false)
                )
            }
            HomeListItem.ViewType.TRANSACTIONS.ordinal -> {
                TransactionsItemViewHolder(
                    LayoutCardTransactionBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> throw IllegalStateException()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.ordinal
    }

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is QuickActionViewHolder -> holder.bind()
            is ItemHeaderViewHolder -> holder.bind(item as HomeListItem.ItemHeaderItem)
            is CardsItemViewHolder -> holder.bind(item as HomeListItem.CardsItem)
            is TransactionsItemViewHolder -> holder.bind(item as HomeListItem.TransactionsItem)
        }
    }

    inner class QuickActionViewHolder(private val binding: LayoutHomeQuickActionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.transferLinearLayout.setOnClickListener {
                callBack?.onItemClick(HomeActionEnum.TRANSFER)
            }
            binding.offerLinearLayout.setOnClickListener {
                callBack?.onItemClick(HomeActionEnum.OFFER)
            }
            binding.currencyLinearLayout.setOnClickListener {
                callBack?.onItemClick(HomeActionEnum.CURRENCY)
            }
        }
    }


    private inner class ItemHeaderViewHolder(private val binding: LayoutItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeListItem.ItemHeaderItem) {
            binding.titleText.text = item.title

            binding.actionTitle.setOnClickListener {
                callBack?.onItemClick(item.action)
            }
        }
    }

    private inner class CardsItemViewHolder(private val binding: LayoutHomeCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: HomeListItem.CardsItem) {
            binding.titleText.text = item.card.cardType
            binding.cardNumText.text = item.card.cardNumber
            binding.amountText.text = "$" + item.card.cardBalance.toString()
            binding.cardDateText.text = item.card.cardDate

            if (item.card.cardType == VISA) {
                binding.iconImage.setImageResource(R.drawable.ic_visa)
            }
            if (item.card.cardType == MASTERCARD){
                binding.iconImage.setImageResource(R.drawable.ic_mastercard)
            }

        }
    }

    private inner class TransactionsItemViewHolder(private val binding: LayoutCardTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeListItem.TransactionsItem) {
            val amount = item.transaction.amount
            val amountNum = amount * -1
            binding.titleText.text = item.transaction.title


            if (amount < 0) {
                binding.amountText.text = amountNum.toString()
                binding.numTypeText.text = "-"
            } else {
                binding.amountText.text = amount.toString()
            }
        }
    }

    interface CallBack {
        fun onItemClick(action: HomeActionEnum)
    }
}
