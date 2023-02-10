package com.example.clearentwrapperdemo.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.clearentwrapperdemo.R
import com.example.clearentwrapperdemo.data.DataStatus
import com.example.clearentwrapperdemo.databinding.FragmentResultBinding

class ResultFragment(
    private val state: DataStatus.ResultMessage,
    private val onDone: () -> Unit
) : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            doneButton.setOnClickListener {
                onDone()
            }

            when (state) {
                is DataStatus.ResultMessage.Error -> {
                    resultTitle.text = state.title
                    resultMessage.text = state.message
                    resultIcon.setImageResource(R.drawable.ic_demo_error)
                }
                is DataStatus.ResultMessage.Success -> {
                    resultTitle.text = state.title
                    resultMessage.text = state.message
                    resultIcon.setImageResource(R.drawable.ic_demo_success)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}