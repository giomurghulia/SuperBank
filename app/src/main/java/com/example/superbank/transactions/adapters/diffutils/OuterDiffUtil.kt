package com.example.superbank.transactions.adapters.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.superbank.transactions.adapters.models.OuterModel

class OuterDiffUtil(): DiffUtil.ItemCallback<OuterModel>(){
    override fun areItemsTheSame(oldItem: OuterModel, newItem: OuterModel) = oldItem.time == newItem.time
    override fun areContentsTheSame(oldItem: OuterModel, newItem: OuterModel) = oldItem == newItem

}