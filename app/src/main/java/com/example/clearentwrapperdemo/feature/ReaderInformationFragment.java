package com.example.clearentwrapperdemo.feature;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clearent.idtech.android.wrapper.ClearentWrapper;
import com.clearent.idtech.android.wrapper.listener.ReaderStatusListener;
import com.clearent.idtech.android.wrapper.model.ReaderState;
import com.clearent.idtech.android.wrapper.model.ReaderStatus;
import com.example.clearentwrapperdemo.R;
import com.example.clearentwrapperdemo.databinding.FragmentReaderInformationBinding;

public class ReaderInformationFragment extends Fragment implements ReaderStatusListener {

    private final ClearentWrapper clearentWrapper = ClearentWrapper.Companion.getInstance();

    private static final String noReaderConnectedMessage = "No reader is connected";
    private static final String readerConnectedMessage = "Connected";
    private static final String readerNotConnectedMessage = "Disconnected";

    private FragmentReaderInformationBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReaderInformationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clearentWrapper.addReaderStatusListener(this);
    }

    private void renderReaderStatus(ReaderState readerState) {
        if (readerState instanceof ReaderState.NoReader) {
            binding.readerName.setText(noReaderConnectedMessage);
            binding.readerConnectionStatus.setVisibility(TextView.GONE);
            binding.readerSignalAndBattery.setVisibility(TextView.GONE);
        } else if (readerState instanceof ReaderState.ReaderUnavailable) {
            binding.readerName.setText(((ReaderState.ReaderUnavailable) readerState).getReader().getDisplayName());
            binding.readerConnectionStatus.setVisibility(TextView.VISIBLE);
            binding.readerConnectionStatus.setText(readerNotConnectedMessage);
            binding.readerSignalAndBattery.setVisibility(TextView.GONE);
        } else if (readerState instanceof ReaderState.ReaderPaired) {
            binding.readerName.setText(((ReaderState.ReaderPaired) readerState).getReader().getDisplayName());
            binding.readerConnectionStatus.setVisibility(TextView.VISIBLE);
            binding.readerConnectionStatus.setText(readerConnectedMessage);
            binding.readerSignalAndBattery.setVisibility(TextView.VISIBLE);
            binding.readerSignalAndBattery.setText(
                    getString(
                            R.string.reader_signal_and_battery_format,
                            ((ReaderState.ReaderPaired) readerState).getBattery().getBatteryLevel(),
                            String.valueOf(((ReaderState.ReaderPaired) readerState).getSignal().getSignalLevel())
                    )
            );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        clearentWrapper.removeReaderStatusListener(this);
    }

    @Override
    public void onReaderStatusUpdate(@Nullable ReaderStatus readerStatus) {
        renderReaderStatus(ReaderState.Companion.fromReaderStatus(readerStatus));
    }
}
