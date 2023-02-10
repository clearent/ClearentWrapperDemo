package com.example.clearentwrapperdemo.feature;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clearentwrapperdemo.databinding.FragmentHomeBinding;

import java.util.function.Consumer;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final Runnable pairReaderHandler;
    private final Consumer<Double> startTransactionHandler;

    public HomeFragment(Runnable pairReaderHandler, Consumer<Double> startTransactionHandler) {
        this.pairReaderHandler = pairReaderHandler;
        this.startTransactionHandler = startTransactionHandler;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews();
    }

    private void setupViews() {
        binding.pairReaderButton.setOnClickListener(view -> pairReaderHandler.run());
        binding.startTransactionButton.setOnClickListener(view ->
                startTransactionHandler.accept(Double.parseDouble(binding.chargeAmountEditText.getText().toString()))
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
