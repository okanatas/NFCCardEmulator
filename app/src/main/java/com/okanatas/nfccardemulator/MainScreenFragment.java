package com.okanatas.nfccardemulator;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.okanatas.nfccardemulator.databinding.FragmentMainScreenBinding;

/**
 * This class was created for the main screen.
 * @author Okan Atas,
 * @version 1.0,
 * created on June 30, 2021
 */
public class MainScreenFragment extends Fragment {

    private FragmentMainScreenBinding binding;
    public static boolean isServiceActivated = false;
    LinearLayout communicationLogsLinearLayout;

    @SuppressLint("StaticFieldLeak")
    private static MainScreenFragment instance = null;

    /**
     * This method is called by Android once the Fragment should inflate a view.
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * This event is triggered soon after onCreateView() and it is only called if the view returned from onCreateView() is non-null.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        instance = this;

        // when view fully created
        binding.currentlyService.setText(InformationTransferManager.getServiceStatus());
        binding.selectedFileText.setText(InformationTransferManager.getSelectedFileText());

        binding.buttonStartStopService.setOnClickListener(this::controlHostCardEmulatorService);

        binding.buttonManageFile.setOnClickListener(v -> navigateToManageFileScreen());

        communicationLogsLinearLayout = view.findViewById(R.id.comm_text_layout);

        reorganizeStartStopServiceButton();
    }

    /**
     * This method is called when the view previously created by onCreateView() has been detached from the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * This method performs validation operations for starting the HCE service.
     * @param view View.
     */
    private void controlHostCardEmulatorService(View view){
        if(InformationTransferManager.getSelectedFileText().equals(InformationTransferManager.getStringResource(R.string.default_selected_file_text))){
            Utils.showSnackBarShort(view, InformationTransferManager.getStringResource(R.string.snack_bar_message_1));
        }else{
            setServiceStatus();
            reorganizeStartStopServiceButton();
        }
    }

    /**
     * This method sets the service status.
     */
    private void setServiceStatus(){
        ((MainActivity) requireActivity()).startStopHostCardEmulatorService(isServiceActivated);
        isServiceActivated = !isServiceActivated;
        binding.currentlyService.setText(InformationTransferManager.getServiceStatus());
    }

    /**
     * This method reorganizes the service button.
     */
    private void reorganizeStartStopServiceButton(){
        if(isServiceActivated){
            binding.buttonStartStopService.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners_color_green));
            binding.buttonStartStopService.setText(R.string.button_stop_service);
        }else{
            if(InformationTransferManager.getSelectedFileText().equals(InformationTransferManager.getStringResource(R.string.default_selected_file_text))){
                binding.buttonStartStopService.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners_color_gray));
            }else{
                binding.buttonStartStopService.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners_color_blue));
            }
            binding.buttonStartStopService.setText(R.string.button_start_service);
        }
    }

    /**
     * This method was created to navigate to the Manage File Screen.
     */
    private void navigateToManageFileScreen(){
        NavHostFragment.findNavController(MainScreenFragment.this)
                .navigate(R.id.action_MainScreenFragment_to_ManageFileScreenFragment);

        Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.manage_file_button_tag), InformationTransferManager.getStringResource(R.string.manage_file_button_text), false);
    }

    /**
     * This method was created to display the communication messages (between terminal and emulated card) on the screen.
     */
    public void showCommunicationMessages(){
        communicationLogsLinearLayout.removeAllViews();
        TextView textView = new TextView(getContext());
        textView.setText(InformationTransferManager.getCardCommunicationMessages());
        textView.setTextSize(14);
        communicationLogsLinearLayout.addView(textView);
        InformationTransferManager.cardCommunicationMessages.setLength(0);
    }

    /**
     * To get MainScreenFragment instance.
     * @return MainScreenFragment instance.
     */
    public static MainScreenFragment getInstance(){
        return instance;
    }
}