package com.example.clearentwrapperdemo.feature;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clearentwrapperdemo.R;
import com.example.clearentwrapperdemo.databinding.FragmentResultBinding;

public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;
    private final ResultMessage resultMessage;
    private final Runnable onDone;

    public ResultFragment(ResultMessage resultMessage, Runnable onDone) {
        this.resultMessage = resultMessage;
        this.onDone = onDone;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews();
    }

    private void setupViews() {
        binding.doneButton.setOnClickListener(view -> onDone.run());
        binding.resultTitle.setText(resultMessage.title);
        binding.resultMessage.setText(resultMessage.message);

        switch (resultMessage.resultType) {
            case SUCCESS:
                binding.resultIcon.setImageResource(R.drawable.ic_demo_success);
                break;
            case FAILURE:
                binding.resultIcon.setImageResource(R.drawable.ic_demo_error);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    public static final class ResultMessage {
        public String title;
        public String message;
        public ResultType resultType;

        public ResultMessage(String title, String message, ResultType resultType) {
            this.title = title;
            this.message = message;
            this.resultType = resultType;
        }
    }

    public enum ResultType {
        SUCCESS, FAILURE
    }
}
