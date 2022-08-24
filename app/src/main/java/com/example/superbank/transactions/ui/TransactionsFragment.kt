package com.example.superbank.transactions.ui

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superbank.R
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentTransactionsBinding
import com.example.superbank.list
import com.example.superbank.transactions.DATE_FORMAT
import com.example.superbank.transactions.adapters.OuterAdapter
import com.example.superbank.transactions.adapters.models.OuterModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TransactionsFragment :
    BaseFragment<FragmentTransactionsBinding>(FragmentTransactionsBinding::inflate) {
    private lateinit var timeList: List<OuterModel>
    private val viewModel: TransactionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timeList = list
    }
    private val adapter by lazy {
        OuterAdapter().apply { submitList(list) }
    }

    override fun listeners() {
        with(binding) {
            income.setOnClickListener {
                var newList: List<OuterModel> = listOf()
                lifecycleScope.launch(Dispatchers.Default) {
                    newList = viewModel.incomeList(timeList)
                }.invokeOnCompletion {
                    adapter.submitList(newList)
                }
                selectIncome()
            }
            expense.setOnClickListener {
                var newList: List<OuterModel> = listOf()
                lifecycleScope.launch(Dispatchers.Default) {
                    newList = viewModel.expenseList(timeList)
                }.invokeOnCompletion {
                    adapter.submitList(newList)
                }
                selectExpense()
            }
            selectorAll.setOnClickListener {
                adapter.submitList(timeList)
                selectAll()
            }
            time.setOnClickListener {
                val myCalendar = Calendar.getInstance()
                val myYear = myCalendar.get(Calendar.YEAR)
                val myMonth = myCalendar.get(Calendar.MONTH)
                val day = myCalendar.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        val text = "$dayOfMonth.${month + 1}.$year"
                        time.text = text
                        var newList: List<OuterModel> = listOf()
                        val format = SimpleDateFormat(DATE_FORMAT)
                        val cal = format.parse(text)
                        lifecycleScope.launch(Dispatchers.Default){
                            newList = viewModel.timeFilter(list, cal!!)
                        }.invokeOnCompletion{
                            timeList = newList
                            adapter.submitList(newList)
                            selectorAll.callOnClick()
                        }
                    },
                    myYear,
                    myMonth,
                    day
                )
                dpd.datePicker.maxDate = System.currentTimeMillis()
                dpd.show()
            }
        }

    }

    override fun bindObservers() {

    }

    override fun init() {
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(context)
    }

    private fun selectIncome(){
        with(binding){
            income.setTextColor(Color.WHITE)
            selectorAll.setTextColor(requireContext().getColor(R.color.not_selected))
            expense.setTextColor(requireContext().getColor(R.color.not_selected))
        }
    }

    private fun selectExpense(){
        with(binding){
            expense.setTextColor(Color.WHITE)
            selectorAll.setTextColor(requireContext().getColor(R.color.not_selected))
            income.setTextColor(requireContext().getColor(R.color.not_selected))
        }
    }

    private fun selectAll(){
        with(binding){
            selectorAll.setTextColor(Color.WHITE)
            income.setTextColor(requireContext().getColor(R.color.not_selected))
            expense.setTextColor(requireContext().getColor(R.color.not_selected))
        }
    }
}