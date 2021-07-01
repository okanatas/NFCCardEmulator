package com.okanatas.nfccardemulator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.okanatas.nfccardemulator.databinding.FragmentAboutScreenBinding;

/**
 * This class was created for the about screen.
 * @author Okan Atas,
 * @version 1.0,
 * created on June 30, 2021
 */
public class AboutScreenFragment extends Fragment {

    private FragmentAboutScreenBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAboutScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}