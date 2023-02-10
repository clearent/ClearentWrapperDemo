package com.example.clearentwrapperdemo.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.clearentwrapperdemo.databinding.FragmentHomeBinding

class HomeFragment(
    private val pairReaderHandler: () -> Unit,
    private val startTransactionHandler: (Double) -> Unit
) : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            pairReaderButton.setOnClickListener {
                pairReaderHandler()
            }
            startTransactionButton.setOnClickListener {
                val chargeAmount: Double? = chargeAmountEditText.text?.toString()?.toDouble()

                chargeAmount?.also {
                    startTransactionHandler(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}