package com.example.superbank.basefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.superbank.SharedViewModel
import com.example.superbank.types.Inflater

abstract class BaseFragment<VB : ViewBinding>(private val inflater: Inflater<VB>) : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = this.inflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.checkAuthorizedUser()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun myToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}