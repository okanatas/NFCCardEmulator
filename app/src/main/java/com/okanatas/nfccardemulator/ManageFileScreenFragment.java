package com.okanatas.nfccardemulator;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.okanatas.nfccardemulator.databinding.FragmentManageFileScreenBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This class was created for the manage file screen.
 * @author Okan Atas,
 * @version 1.0,
 * created on June 30, 2021
 */
public class ManageFileScreenFragment extends Fragment {

    private FragmentManageFileScreenBinding binding;
    private static String streamContent = "";
    private static String userEnteredFileName = "";

    ArrayList<String> fileSpinnerData = new ArrayList<>();
    private String fileOnSpinner = "";

    /**
     * This is for to start file chooser activity and receive the result back.
     */
    ActivityResultLauncher<String> getChosenFileContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if(uri != null){
                    try {
                        // get content of the selected file
                        InputStream stream = requireContext().getContentResolver().openInputStream(uri);
                        // convert content to String format
                        streamContent = Utils.inputStreamToString(stream);
                        binding.userInputFilename.setText(getFileNameWithoutExtension(uri));
                        visibilityOfRenameFile();
                        reorganizeImportButton();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

    /**
     * This method is called by Android once the Fragment should inflate a view.
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentManageFileScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * This event is triggered soon after onCreateView() and it is only called if the view returned from onCreateView() is non-null.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonBrowse.setOnClickListener(v -> startBrowse());
        binding.buttonImport.setOnClickListener(this::fileImportManager);
        binding.buttonSetFile.setOnClickListener(this::useSelectedFile);
        binding.buttonDeleteFile.setOnClickListener(this::deleteSelectedFile);

        setFilesSpinner();

        binding.filesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                onItemSelectedCase(parentView, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }
        });

        clearResources();
        visibilityOfRenameFile();
        reorganizeImportButton();
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
     * This method creates a copy of the selected file within the application files.
     * @param view View.
     */
    private void fileImportManager(View view) {
        userEnteredFileName = binding.userInputFilename.getText().toString().trim();

        if(validateImport(view)){
            // create a new file in app files directory
            File targetFile = new File(InformationTransferManager.getAppFilesDirectory() + "/" + userEnteredFileName + ".txt");
            // write uri content into the newly created file
            ((MainActivity) requireActivity()).writeToFile(targetFile.getName(), streamContent);
            Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.import_tag), InformationTransferManager.getStringResource(R.string.import_text_1)
                    + " \"" + userEnteredFileName + ".txt\".", false);
            setFilesSpinner();
            Utils.showSnackBarShort(view, userEnteredFileName + ".txt " + InformationTransferManager.getStringResource(R.string.import_text_2));
            clearResources();
            visibilityOfRenameFile();
            reorganizeImportButton();
        }
    }

    /**
     * This method validates to import action.
     * @param view View.
     * @return boolean that shows validation result.
     */
    private static boolean validateImport(View view){
        boolean result = true;
        if(userEnteredFileName.equals("")){
            Utils.showSnackBarLong(view, InformationTransferManager.getStringResource(R.string.not_imported_missing_filename));
            result = false;
        }

        if(userEnteredFileName.contains(".")){
            Utils.showSnackBarLong(view, InformationTransferManager.getStringResource(R.string.not_imported_extension_entered));
            result = false;
        }

        if (streamContent.equals("")){
            Utils.showSnackBarLong(view, InformationTransferManager.getStringResource(R.string.not_imported_missing_source_file));
            result = false;
        }
        return result;
    }

    /**
     * This method clears the resources.
     */
    private void clearResources(){
        // clear
        userEnteredFileName = "";
        streamContent = "";
        binding.userInputFilename.setText("");
    }

    /**
     * This is the trigger method that starts the browse process.
     */
    private void startBrowse(){
        Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.browse_tag), InformationTransferManager.getStringResource(R.string.browse_text), false);
        getChosenFileContent.launch("text/*");
    }

    /**
     * This method gives the option to give a new name when a file is selected for import.
     */
    private void visibilityOfRenameFile(){
        if(streamContent.equals("")){
            binding.renameFileTitle.setVisibility(View.INVISIBLE);
            binding.userInputFilename.setVisibility(View.INVISIBLE);
        }else{
            binding.renameFileTitle.setVisibility(View.VISIBLE);
            binding.userInputFilename.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method returns the original name of the browsed file.
     * @return the original name of the browsed file.
     */
    private String getFileNameWithoutExtension(Uri uri){
        String[] parsedFileName = new String[2];
        String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
        ContentResolver cr = requireContext().getContentResolver();
        Cursor metaCursor = cr.query(uri, projection, null, null, null);
        if (metaCursor != null) {
            try {
                if (metaCursor.moveToFirst()) {
                    parsedFileName = (metaCursor.getString(0)).split("\\.");
                }
            } finally {
                metaCursor.close();
            }
        }
        return parsedFileName[0];
    }

    /**
     * This method writes the existing filenames on the spinner.
     */
    private void setFilesSpinner(){
        setFileSpinnerData();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, fileSpinnerData);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.filesSpinner.setAdapter(dataAdapter);

        reorganizeFileSelectionButtons();
    }

    /**
     * This method writes the existing filenames to the ArrayList.
     */
    private void setFileSpinnerData(){
        //clear fileOnSpinner
        fileOnSpinner = "";

        File[] files = InformationTransferManager.getApplicationFileList();
        if(files != null){
            fileSpinnerData.clear();
            for(File file : files){
                fileSpinnerData.add(file.getName());
            }
        }
    }

    /**
     * This method was created to detect the filename the spinner is pointing to.
     */
    private void onItemSelectedCase(AdapterView<?> parentView, int position){
        Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.on_item_selected_tag), InformationTransferManager.getStringResource(R.string.on_item_selected_text) + " \"" + parentView.getItemAtPosition(position) + "\".", false);
        fileOnSpinner = (String) parentView.getItemAtPosition(position);
    }

    /**
     * This method manages the operations of reading, processing, and transmitting the command
     * and response data from the file selected to be used in the HCE service to other related methods.
     * @param view View.
     */
    private void useSelectedFile(View view){
        if(!fileOnSpinner.equals("")){
            MainActivity.FILE_NAME = fileOnSpinner;
            String fileContentInText = ((MainActivity) requireActivity()).readFromFile(view);
            FileHandler.setCommandsAndResponses(fileContentInText);

            // set command size
            InformationTransferManager.setCommandSize(FileHandler.commands.size());
            // set response size
            InformationTransferManager.setResponseSize(FileHandler.responses.size());

            InformationTransferManager.setSelectedFileText(fileOnSpinner);
            Utils.showSnackBarShort(view, fileOnSpinner + " " + InformationTransferManager.getStringResource(R.string.snack_bar_message_2));
            Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.select_file_tag), "\"" + fileOnSpinner + "\" "
                    + InformationTransferManager.getStringResource(R.string.snack_bar_message_3) + " "
                    + InformationTransferManager.getStringResource(R.string.snack_bar_message_4) + " "
                    + InformationTransferManager.getCommandSize() + " "
                    + InformationTransferManager.getStringResource(R.string.snack_bar_message_5) + " "
                    + InformationTransferManager.getResponseSize(), false);
        }else{
            Utils.showSnackBarShort(view, InformationTransferManager.getStringResource(R.string.snack_bar_no_file_to_select));
        }
    }

    /**
     * This method takes over the task of deleting the file selected with the spinner.
     * @param view View.
     */
    private void deleteSelectedFile(View view){
        if(!fileOnSpinner.equals("")){
            Utils.deleteFile(InformationTransferManager.getAppFilesDirectory(), fileOnSpinner);
            Utils.showSnackBarShort(view, fileOnSpinner + " "
                    + InformationTransferManager.getStringResource(R.string.snack_bar_message_6));
            Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.delete_file_tag), "\"" + fileOnSpinner + "\" "
                    + InformationTransferManager.getStringResource(R.string.snack_bar_message_6), false);
            checkIfUsingFileDeleted();
            setFilesSpinner();
        }else{
            Utils.showSnackBarShort(view, InformationTransferManager.getStringResource(R.string.snack_bar_no_file_to_delete));
        }
    }

    /**
     * This method checks whether the currently in use file has been deleted.
     */
    private void checkIfUsingFileDeleted(){
        if(fileOnSpinner.equals(InformationTransferManager.getSelectedFileText())){
            // clean all commands and responses
            FileHandler.commands.clear();
            FileHandler.responses.clear();

            // set selected file for no file
            InformationTransferManager.setSelectedFileText("");
            // notice
            Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.notice_tag), InformationTransferManager.getStringResource(R.string.notice_text), false);

            // if the service active set the status
            if(MainScreenFragment.isServiceActivated){
                setServiceStatus();
            }
        }
    }

    /**
     * This method sets the service status.
     */
    private void setServiceStatus(){
        ((MainActivity) requireActivity()).startStopHostCardEmulatorService(MainScreenFragment.isServiceActivated);
        MainScreenFragment.isServiceActivated = !MainScreenFragment.isServiceActivated;
    }

    /**
     * This method reorganizes (color and shape) the import button.
     */
    private void reorganizeImportButton(){
        if(streamContent.equals("")){
            binding.buttonImport.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners_color_gray));
        }else{
            binding.buttonImport.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners_color_blue));
        }
    }

    /**
     * This method reorganizes (color and shape) the file selection buttons.
     */
    private void reorganizeFileSelectionButtons(){
        if(fileSpinnerData.size() == 0){
            binding.buttonSetFile.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners_color_gray));
            binding.buttonDeleteFile.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners_color_gray));
        }else{
            binding.buttonSetFile.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners_color_blue));
            binding.buttonDeleteFile.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corners_color_blue));
        }
    }

}