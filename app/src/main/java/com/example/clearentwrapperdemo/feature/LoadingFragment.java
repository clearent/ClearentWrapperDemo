package com.example.clearentwrapperdemo.feature;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clearentwrapperdemo.databinding.FragmentLoadingBinding;

import timber.log.Timber;

public class LoadingFragment extends Fragment {

    private FragmentLoadingBinding binding;

    private final String title;
    private final String message;

    public LoadingFragment(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoadingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.post(() -> {
            Timber.d("TESTEST LOADING FRAGMENT " + title + " " + message);
            setupViews();
        });
    }

    private void setupViews() {
        binding.loadingTitle.setText(title);
        binding.loadingMessage.setText(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
