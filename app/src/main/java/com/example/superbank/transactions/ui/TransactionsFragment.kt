package com.example.superbank.transactions.ui

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superbank.R
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentTransactionsBinding
import com.example.superbank.networking.responsestate.ResponseState
import com.example.superbank.transactions.DATE_FORMAT
import com.example.superbank.transactions.adapters.OuterAdapter
import com.example.superbank.transactions.adapters.models.OuterModel
import com.example.superbank.transactions.getmodel.toOuterList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TransactionsFragment :
    BaseFragment<FragmentTransactionsBinding>(FragmentTransactionsBinding::inflate) {
    companion object {
        private var list: List<OuterModel> = listOf()
    }

    private var timeList: List<OuterModel> = list
    private val viewModel: TransactionsViewModel by viewModels()

    private val handler = Handler()
    private val recyclerScrollRunnable = Runnable {
        binding.recycler.smoothScrollToPosition(0)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        listeners()
        bindObservers()
    }

    private val adapter by lazy {
        OuterAdapter().apply {
            submitList(list)
            setOnOuterItemClickListener { title: String, type: String, amount: String, description: String, cardLastDigits: String, date: String ->
                findNavController().navigate(
                    TransactionsFragmentDirections.actionTransactionFragmentToTransactionInfoFragment(
                        type,
                        amount,
                        date,
                        description,
                        cardLastDigits,
                        title
                    )
                )
            }
        }
    }

    override fun listeners() {
        with(binding) {
            income.setOnClickListener {
                var newList: List<OuterModel> = listOf()
                lifecycleScope.launch(Dispatchers.Default) {
                    newList = viewModel.incomeList(timeList)
                }.invokeOnCompletion {
                    adapter.submitList(newList)

                    scrollToTop()
                }
                selectIncome()
            }
            expense.setOnClickListener {
                var newList: List<OuterModel> = listOf()
                lifecycleScope.launch(Dispatchers.Default) {
                    newList = viewModel.expenseList(timeList)
                }.invokeOnCompletion {
                    adapter.submitList(newList)

                    scrollToTop()
                }
                selectExpense()
            }
            selectorAll.setOnClickListener {
                adapter.submitList(timeList)
                selectAll()

                scrollToTop()
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
                        lifecycleScope.launch(Dispatchers.Default) {
                            newList = viewModel.timeFilter(list, cal!!)
                        }.invokeOnCompletion {
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
            refresh.setOnRefreshListener {
                bindObservers()
                time.text = getString(R.string.select_time_range)
                selectorAll.callOnClick()
            }
        }

    }

    override fun bindObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transactionsListFlow.collect {
                    when (it) {
                        is ResponseState.Success -> {
                            if (it.body.isNotEmpty()) {
                                list = it.body.toOuterList()
                                timeList = list
                                adapter.submitList(list.toList())
                                with(binding) {
                                    selectorAll.isClickable = true
                                    expense.isClickable = true
                                    income.isClickable = true
                                }
                            }
                        }
                        is ResponseState.Error -> {
                            myToast(it.errorMessage)
                        }
                        is ResponseState.Load -> {
                            with(binding) {
                                selectorAll.isClickable = false
                                expense.isClickable = false
                                income.isClickable = false
                            }
                        }
                    }
                    binding.refresh.isRefreshing = false
                }
            }
        }
    }

    override fun init() {
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(context)
        if (list.isEmpty())
            viewModel.getTransactions()
    }

    private fun selectIncome() {
        with(binding) {
            income.setTextColor(Color.WHITE)
            selectorAll.setTextColor(requireContext().getColor(R.color.not_selected))
            expense.setTextColor(requireContext().getColor(R.color.not_selected))

            income.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.not_selected))
            selectorAll.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.filter_selector_color))
            expense.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.filter_selector_color))
        }
    }

    private fun selectExpense() {
        with(binding) {
            expense.setTextColor(Color.WHITE)
            selectorAll.setTextColor(requireContext().getColor(R.color.not_selected))
            income.setTextColor(requireContext().getColor(R.color.not_selected))

            expense.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.not_selected))
            income.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.filter_selector_color))
            selectorAll.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.filter_selector_color))
        }
    }

    private fun selectAll() {
        with(binding) {
            selectorAll.setTextColor(Color.WHITE)
            income.setTextColor(requireContext().getColor(R.color.not_selected))
            expense.setTextColor(requireContext().getColor(R.color.not_selected))

            selectorAll.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.not_selected))
            income.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.filter_selector_color))
            expense.backgroundTintList =
                ColorStateList.valueOf(requireContext().getColor(R.color.filter_selector_color))
        }
    }

    private fun scrollToTop() {
        handler.postDelayed(recyclerScrollRunnable, 300)
    }

    override fun onDestroyView() {
        handler.removeCallbacks(recyclerScrollRunnable)
        super.onDestroyView()
    }
}