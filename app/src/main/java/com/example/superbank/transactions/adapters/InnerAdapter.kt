package com.example.superbank.transactions.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.superbank.R
import com.example.superbank.databinding.InnerRecyclerItemBinding
import com.example.superbank.transactions.adapters.diffutils.InnerDiffUtil
import com.example.superbank.transactions.adapters.models.InnerModel

class InnerAdapter :
    ListAdapter<InnerModel, InnerAdapter.InnerViewHolder>(InnerDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InnerViewHolder(
        InnerRecyclerItemBinding.inflate(LayoutInflater.from(parent.context))
    )

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class InnerViewHolder(private val binding: InnerRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val model = getItem(position)
            with(binding) {
                icon.setImageResource(model.type.icon())
                tittle.text = model.title
                amount.text = if (model.amount > 0) "$${model.amount}" else "-$${-model.amount}"
                line.setBackgroundColor( if(position == itemCount - 1) root.context.getColor(R.color.black) else root.context.getColor(R.color.line_separator))
            }
        }
    }
}

