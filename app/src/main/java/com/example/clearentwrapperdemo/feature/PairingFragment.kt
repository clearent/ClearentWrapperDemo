package com.example.clearentwrapperdemo.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.clearent.idtech.android.wrapper.model.ReaderStatus
import com.example.clearentwrapperdemo.data.DataStatus
import com.example.clearentwrapperdemo.databinding.FragmentPairingBinding

class PairingFragment(
    private val state: DataStatus.ReadersList,
    private val selectReader: (ReaderStatus) -> Unit,
    private val cancel: () -> Unit
) : Fragment(), ReadersListAdapter.OnItemClickListener {

    private var _binding: FragmentPairingBinding? = null
    private val binding get() = _binding!!

    private val readersListAdapter = ReadersListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPairingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            readersList.apply {
                adapter = readersListAdapter
                layoutManager = LinearLayoutManager(requireContext())
                readersListAdapter.submitList(state.list)

                if (state.list.isEmpty())
                    pairingNoReadersInfo.visibility = View.VISIBLE
                else
                    pairingNoReadersInfo.visibility = View.GONE

                pairingCancelButton.setOnClickListener {
                    cancel()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(reader: ReaderStatus) {
        selectReader(reader)
    }
}