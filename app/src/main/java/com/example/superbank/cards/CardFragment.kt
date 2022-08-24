package com.example.superbank.cards

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.superbank.databinding.FragmentMyCardsBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class CardFragment : Fragment() {
    private lateinit var binding: FragmentMyCardsBinding

    private val viewModel: CardViewModel by viewModels()

    private val cardsAdapter = CardsPagerAdapter()
    private val cardDescriptionAdapter = CardDescriptionAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMyCardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    binding.mainRecycler.postDelayed({
                        binding.mainRecycler.smoothScrollToPosition(0)
                    }, 300)
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

        cardDescriptionAdapter.setCallBack(object : CardDescriptionAdapter.CallBack {
            override fun onItemClick(itemId: QuickActionEnum) {
                viewModel.onItemClick(itemId)
            }
        })
    }

    private fun makeAlertDialog(alert:String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Super Bank")
        builder.setMessage(alert)
        val newAlert = builder.create()
        newAlert.show()
    }
}