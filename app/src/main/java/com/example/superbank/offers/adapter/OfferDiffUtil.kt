package com.example.superbank.offers.adapter

import androidx.recyclerview.widget.DiffUtil

class OfferDiffUtil : DiffUtil.ItemCallback<OfferModel>() {
    override fun areItemsTheSame(oldItem: OfferModel, newItem: OfferModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: OfferModel, newItem: OfferModel) = oldItem == newItem
}