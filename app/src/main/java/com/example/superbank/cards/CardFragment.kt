package com.example.superbank.cards

import android.app.AlertDialog
import android.os.Handler
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.superbank.basefragments.BaseFragment
import com.example.superbank.databinding.FragmentCardsBinding
import com.example.superbank.home.HomeActionEnum
import com.example.superbank.home.HomeFragmentDirections
import com.example.superbank.transactions.adapters.models.InnerModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class CardFragment : BaseFragment<FragmentCardsBinding>(
    FragmentCardsBinding::inflate
) {

    private val viewModel: CardViewModel by viewModels()

    private val cardsAdapter = CardsPagerAdapter()
    private val cardDescriptionAdapter = CardDescriptionAdapter()

    private val handler = Handler()
    private val recyclerScrollRunnable = Runnable {
        binding.mainRecycler.smoothScrollToPosition(0)
    }

    override fun init() {
        viewModel.getCards()

        binding.cardViewpager.adapter = cardsAdapter
        binding.cardViewpager.setPageTransformer(MarginPageTransformer(40))
        binding.cardViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                viewModel.onCardSelected(position)
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.cardViewpager) { tab, position ->
            //Some implementation
        }.attach()

        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = cardDescriptionAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cards.collect {
                    cardsAdapter.submitList(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listItems.collect {
                    cardDescriptionAdapter.submitList(it)
                    handler.postDelayed(recyclerScrollRunnable, 300)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.alertDialog.collect {
                    makeAlertDialog(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.action.collect {
                    sharedViewModel.homeAction(HomeActionEnum.ALL_TRANSACTION)
                }
            }
        }

        cardDescriptionAdapter.setCallBack(object : CardDescriptionAdapter.CallBack {
            override fun onItemClick(itemId: QuickActionEnum) {
                viewModel.onItemClick(itemId)
            }

            override fun onTransactionClick(item: InnerModel) {
                findNavController().navigate(
                    HomeFragmentDirections.actionGlobalTransactionInfoFragment(item)
                )
            }
        })
    }

    override fun onDestroyView() {
        handler.removeCallbacks(recyclerScrollRunnable)
        super.onDestroyView()
    }

    private fun makeAlertDialog(alert: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Super Bank")
        builder.setMessage(alert)
        val newAlert = builder.create()
        newAlert.show()
    }
}