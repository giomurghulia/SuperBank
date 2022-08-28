package com.example.superbank.offers.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.superbank.databinding.OfferItemBinding
import com.example.superbank.extensions.load

class OffersAdapter : ListAdapter<OfferModel, OffersAdapter.OfferViewHolder>(OfferDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OfferViewHolder(OfferItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class OfferViewHolder(private val binding: OfferItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val model = getItem(position)
            with(binding) {
                image.load(model.avatar)
                tittle.text = model.title
                image.setOnClickListener {
                    val url =
                        if (!model.url.startsWith("http://") && !model.url.startsWith("https://"))
                            "http://${model.url}"
                        else model.url
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    it.context.startActivity(intent)
                }
            }
        }
    }
}