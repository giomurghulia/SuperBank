package com.example.superbank.basefragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.superbank.R
import com.example.superbank.SharedViewModel
import com.example.superbank.types.Inflater

abstract class BaseFragment<VB : ViewBinding>(private val inflater: Inflater<VB>) : Fragment() {
    val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    open fun init() {}
    open fun listeners() {}
    open fun bindObservers() {}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)

        _binding = this.inflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isOnline()) {
            sharedViewModel.checkAuthorizedUser()
            init()
            listeners()
            bindObservers()
        } else {
            sharedViewModel.onNoInternet()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun myToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }


    fun isOnline(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null
    }
}