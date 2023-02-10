package com.example.clearentwrapperdemo.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.clearent.idtech.android.wrapper.ClearentWrapper
import com.clearent.idtech.android.wrapper.listener.ReaderStatusListener
import com.clearent.idtech.android.wrapper.model.ReaderState
import com.clearent.idtech.android.wrapper.model.ReaderStatus
import com.example.clearentwrapperdemo.R
import com.example.clearentwrapperdemo.databinding.FragmentReaderInformationBinding
import kotlinx.coroutines.launch

class ReaderInformationFragment : Fragment(), ReaderStatusListener {

    companion object {
        private const val noReaderConnectedMessage = "No reader is connected"
        private const val readerConnectedMessage = "Connected"
        private const val readerNotConnectedMessage = "Disconnected"
    }

    private var _binding: FragmentReaderInformationBinding? = null
    private val binding get() = _binding!!

    private val clearentWrapper = ClearentWrapper.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReaderInformationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearentWrapper.addReaderStatusListener(this)
    }

    private fun renderReaderStatus(readerState: ReaderState) = lifecycleScope.launch {
        binding.apply {
            when (readerState) {
                ReaderState.NoReader -> {
                    readerName.text = noReaderConnectedMessage
                    readerConnectionStatus.visibility = TextView.GONE
                    readerSignalAndBattery.visibility = TextView.GONE
                }
                is ReaderState.ReaderUnavailable -> {
                    readerName.text = readerState.reader.displayName
                    readerConnectionStatus.visibility = TextView.VISIBLE
                    readerConnectionStatus.text = readerNotConnectedMessage
                    readerSignalAndBattery.visibility = TextView.GONE
                }
                is ReaderState.ReaderPaired -> {
                    readerName.text = readerState.reader.displayName
                    readerConnectionStatus.visibility = TextView.VISIBLE
                    readerConnectionStatus.text = readerConnectedMessage
                    readerSignalAndBattery.visibility = TextView.VISIBLE
                    readerSignalAndBattery.text = getString(
                        R.string.reader_signal_and_battery_format,
                        readerState.battery.batteryLevel,
                        readerState.signal.signalLevel.toString()
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        clearentWrapper.removeReaderStatusListener(this)
    }

    override fun onReaderStatusUpdate(readerStatus: ReaderStatus?) {
        renderReaderStatus(ReaderState.fromReaderStatus(readerStatus))
    }
}