package com.example.superbank.transactions.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.superbank.databinding.InnerRecyclerItemBinding
import com.example.superbank.transactions.adapters.diffutils.InnerDiffUtil
import com.example.superbank.transactions.adapters.models.InnerModel

class InnerAdapter :
    ListAdapter<InnerModel, InnerAdapter.InnerViewHolder>(InnerDiffUtil()) {
    private lateinit var clickListener: (title: String, type: String, amount: String, description: String, cardLastDigits: String) -> Unit

    fun setOnItemClickListener(listener: (title: String, type: String, amount: String, description: String, cardLastDigits: String) -> Unit) {
        clickListener = listener
    }

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
                root.setOnClickListener {
                    if (this@InnerAdapter::clickListener.isInitialized)
                        clickListener(
                            model.title,
                            model.type.toString(),
                            model.amount.toString(),
                            model.description,
                            model.cardLastDigits
                        )
                }
                icon.setImageResource(model.type.icon())
                icon.backgroundTintList = ColorStateList.valueOf(root.context.getColor(model.type.backgroundTint))
                title.text = model.title
                amount.text = if (model.amount > 0) "$${model.amount}" else "-$${-model.amount}"
            }
        }
    }
}

