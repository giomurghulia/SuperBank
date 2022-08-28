package com.example.superbank.cards

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.superbank.MainDiffUtil
import com.example.superbank.databinding.LayoutCardTransactionBinding
import com.example.superbank.databinding.LayoutItemHeaderBinding
import com.example.superbank.databinding.LayoutQuickActionBinding
import com.example.superbank.databinding.LayoutUpcomingPaymentBinding

class CardDescriptionAdapter :
    ListAdapter<CardDescriptionListItem, RecyclerView.ViewHolder>(MainDiffUtil()) {
    private var callBack: CallBack? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            CardDescriptionListItem.ViewType.UPCOMING_PAYMENT.ordinal -> {
                UpcomingPaymentViewHolder(
                    LayoutUpcomingPaymentBinding.inflate(layoutInflater, parent, false)
                )
            }
            CardDescriptionListItem.ViewType.QUICK_ACTION.ordinal -> {
                QuickActionViewHolder(
                    LayoutQuickActionBinding.inflate(layoutInflater, parent, false)
                )
            }
            CardDescriptionListItem.ViewType.CARD_TRANSACTIONS.ordinal -> {
                CardTransactionViewHolder(
                    LayoutCardTransactionBinding.inflate(layoutInflater, parent, false)
                )
            }
            CardDescriptionListItem.ViewType.ITEM_HEADER.ordinal -> {
                ItemHeaderViewHolder(
                    LayoutItemHeaderBinding.inflate(layoutInflater, parent, false)
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
            is UpcomingPaymentViewHolder -> holder.bind(item as CardDescriptionListItem.UpcomingPaymentItem)
            is QuickActionViewHolder -> holder.bind()
            is CardTransactionViewHolder -> holder.bind(item as CardDescriptionListItem.CardTransactionsItem)
            is ItemHeaderViewHolder -> holder.bind(item as CardDescriptionListItem.ItemHeaderItem)
        }
    }


    inner class UpcomingPaymentViewHolder(private val binding: LayoutUpcomingPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CardDescriptionListItem.UpcomingPaymentItem) {
            binding.amountText.text = item.payment.amount.toString()
            binding.dueDateText.text = item.payment.dueDate
        }
    }

    inner class QuickActionViewHolder(private val binding: LayoutQuickActionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.cardInfoLayout.setOnClickListener {
                callBack?.onItemClick(QuickActionEnum.CARD_INFO)
            }
            binding.changePinLayout.setOnClickListener {
                callBack?.onItemClick(QuickActionEnum.CHANGE_PIN)
            }
            binding.removeCardLayout.setOnClickListener {
                callBack?.onItemClick(QuickActionEnum.REMOVE_CARD)
            }
        }
    }

    inner class CardTransactionViewHolder(private val binding: LayoutCardTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CardDescriptionListItem.CardTransactionsItem) {
            binding.amountText.text =
                if (item.amount > 0) "$${item.amount}" else "-$${-item.amount}"
            binding.titleText.text = item.type.toString()

            binding.iconImage.setImageResource(item.type.icon())
            binding.iconImage.backgroundTintList =
                ColorStateList.valueOf(binding.root.context.getColor(item.type.backgroundTint))
        }
    }

    inner class ItemHeaderViewHolder(private val binding: LayoutItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CardDescriptionListItem.ItemHeaderItem) {
            binding.titleText.text = item.title

            binding.actionTitle.setOnClickListener {
                callBack?.onItemClick(QuickActionEnum.All_TRANSACTION)
            }
        }
    }

    interface CallBack {
        fun onItemClick(itemId: QuickActionEnum)
    }
}