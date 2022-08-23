package com.example.superbank.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.superbank.MainDiffUtil
import com.example.superbank.databinding.LayoutQuickActionBinding
import com.example.superbank.databinding.LayoutUpcomingPaymentBinding

class MyCardsAdapter : ListAdapter<MyCardsListItem, RecyclerView.ViewHolder>(MainDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            MyCardsListItem.ViewType.UPCOMING_PAYMENT.ordinal -> {
                UpcomingPaymentViewHolder(
                    LayoutUpcomingPaymentBinding.inflate(layoutInflater, parent, false)
                )
            }
            MyCardsListItem.ViewType.QUICK_ACTION.ordinal -> {
                QuickActionViewHolder(
                    LayoutQuickActionBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> throw IllegalStateException()

        }

    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is UpcomingPaymentViewHolder -> holder.bind(item as MyCardsListItem.UpcomingPaymentItem)
            is QuickActionViewHolder -> holder.bind()
        }
    }


    inner class UpcomingPaymentViewHolder(private val binding: LayoutUpcomingPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyCardsListItem.UpcomingPaymentItem) {
            binding.amountText.text = item.payment.amount.toString()
            binding.dueDateText.text = item.payment.dueDate
        }
    }

    inner class QuickActionViewHolder(private val binding: LayoutQuickActionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
        }
    }
}