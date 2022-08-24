package com.example.superbank.transactions.adapters.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.superbank.transactions.adapters.models.InnerModel

class InnerDiffUtil : DiffUtil.ItemCallback<InnerModel>() {
    override fun areItemsTheSame(
        oldItem: InnerModel,
        newItem: InnerModel
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: InnerModel,
        newItem: InnerModel
    ) = oldItem == newItem

}