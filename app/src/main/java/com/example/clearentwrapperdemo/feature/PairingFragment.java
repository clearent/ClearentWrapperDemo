package com.example.clearentwrapperdemo.feature;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.clearent.idtech.android.wrapper.model.ReaderStatus;
import com.example.clearentwrapperdemo.databinding.FragmentPairingBinding;

import java.util.List;
import java.util.function.Consumer;

public class PairingFragment extends Fragment implements ReadersListAdapter.ItemClickListener {

    private FragmentPairingBinding binding;

    private final List<ReaderStatus> readers;
    private final Consumer<ReaderStatus> selectReader;
    private final Runnable cancel;

    public PairingFragment(List<ReaderStatus> list, Consumer<ReaderStatus> selectReader, Runnable cancel) {
        this.readers = list;
        this.selectReader = selectReader;
        this.cancel = cancel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPairingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews();
    }

    private void setupViews() {
        ReadersListAdapter adapter = new ReadersListAdapter(readers, this);
        binding.readersList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.readersList.setAdapter(adapter);
        if (readers.isEmpty())
            binding.pairingNoReadersInfo.setVisibility(View.VISIBLE);
        else
            binding.pairingNoReadersInfo.setVisibility(View.GONE);
        binding.pairingCancelButton.setOnClickListener(view -> cancel.run());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onItemClick(ReaderStatus readerStatus) {
        selectReader.accept(readerStatus);
    }
}
