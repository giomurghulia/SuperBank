package com.example.superbank.basefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.superbank.types.Inflater

abstract class BaseFragment<VB : ViewBinding>(private val inflater: Inflater<VB>) : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!
    abstract fun listeners()
    abstract fun bindObservers()
    abstract fun init()

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
        init()
        listeners()
        bindObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}