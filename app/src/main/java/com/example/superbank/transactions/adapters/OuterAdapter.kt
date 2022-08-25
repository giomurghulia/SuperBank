package com.example.superbank.transactions.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.recyclerview.widget.*
import com.example.superbank.databinding.OuterRecyclerItemBinding
import com.example.superbank.transactions.adapters.diffutils.OuterDiffUtil
import com.example.superbank.transactions.adapters.models.OuterModel

class OuterAdapter : ListAdapter<OuterModel, OuterAdapter.OuterViewHolder>(OuterDiffUtil()) {
    private lateinit var clickListener: (title: String, type: String, amount: String, description: String, cardLastDigits: String, date: String) -> Unit

    fun setOnOuterItemClickListener(listener: (title: String, type: String, amount: String, description: String, cardLastDigits: String, date: String) -> Unit) {
        clickListener = listener
    }

    companion object {
        private const val MARGIN_IN_DP = 20
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OuterViewHolder(
        OuterRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context)
        )
    )


    override fun onBindViewHolder(holder: OuterViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class OuterViewHolder(private val binding: OuterRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val displayMetrics = binding.root.context.resources.displayMetrics
            val density = displayMetrics.density
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                (displayMetrics.widthPixels - 2 * (MARGIN_IN_DP * density)).toInt(),
                Toolbar.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, (MARGIN_IN_DP * density).toInt(), 0, 0)
            val model = getItem(position)
            with(binding) {
                date.text = model.time
                val manager = LinearLayoutManager(binding.root.context)
                val adapter = InnerAdapter().apply {
                    setOnItemClickListener { title: String, type: String, amount: String, description: String, cardLastDigits: String ->
                        if (this@OuterAdapter::clickListener.isInitialized)
                            clickListener(title, type, amount, description, cardLastDigits, model.time)
                    }
                }
                innerRecycler.layoutParams = params
                adapter.submitList(model.transactions)
                innerRecycler.layoutManager = manager
                innerRecycler.adapter = adapter
            }
        }
    }
}


