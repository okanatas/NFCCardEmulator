package com.okanatas.nfccardemulator;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.okanatas.nfccardemulator.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class was created to control transitions between fragments
 * and support the existence of the toolbar and send mail button for each screen.
 * @author Okan Atas,
 * @version 1.0,
 * created on June 30, 2021
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    AlertDialog.Builder dialogBuilder;
    public static String FILE_NAME;

    /**
     * When the application is still working, it starts a different activity and the application is informed about the result.
     * For this case, the user is asked what to do with the log messages when the result is received.
     */
    ActivityResultLauncher<Intent> startForEmailActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            activityResult -> {
                Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.share_message_tag), InformationTransferManager.getStringResource(R.string.share_message_text_2), false);
                showDialogForLogMessages();
            });


    /**
     * MainActivity onCreate method
     * @param savedInstanceState activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(v -> startForEmailActivity.launch(getEmailIntent()));

        // alert dialog for log messages action
        dialogBuilder = new AlertDialog.Builder(this);

        // set application files directory
        InformationTransferManager.setAppFilesDirectory(String.valueOf(getFilesDir()));
    }

    /**
     * This method created to specify the options menu for an activity.
     * @param menu the options menu.
     * @return true for the menu to be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This method created to catch whenever an item in the options menu is selected.
     * @param item selected item.
     * @return boolean response for menu processing.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.action_about):
                navigateToAboutScreen();
                return true;
            case (R.id.action_help):
                navigateToHelpScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method is called whenever the user chooses to navigate Up within the application.
     * @return boolean response for navigate up.
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.navigate_up_tag), InformationTransferManager.getStringResource(R.string.move_back_text), false);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * This method is called when a key was pressed down.
     * @param keyCode the value.
     * @param event description of the key event.
     * @return boolean response for onKeyDown.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.on_key_down_tag), InformationTransferManager.getStringResource(R.string.move_back_text), false);
        return super.onKeyDown(keyCode, event);
    }

    /**
     * This method was created to be the starting point for starting and stopping the HCE service.
     * @param isServiceActivated boolean to check current status of the service.
     */
    public void startStopHostCardEmulatorService(boolean isServiceActivated){
        Intent intent = new Intent(this, HostCardEmulatorService.class);

        if(!isServiceActivated){
            startService(intent);
            Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.start_service_tag), InformationTransferManager.getStringResource(R.string.start_service_text), false);
        }else{
            stopService(intent);
            Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.stop_service_tag), InformationTransferManager.getStringResource(R.string.stop_service_text), false);
        }
    }

    /**
     * This method creates an email intent.
     * @return email intent.
     */
    @SuppressLint("IntentReset")
    private Intent getEmailIntent(){
        Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.share_message_tag),
                InformationTransferManager.getStringResource(R.string.share_message_text_1), false);
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, InformationTransferManager.getStringResource(R.string.hce_email_title));
        emailIntent.putExtra(Intent.EXTRA_TEXT, InformationTransferManager.getTotalLogMessages());

        return emailIntent;
    }

    /**
     * This method writes the given text into the target file.
     * @param targetFileName target file name.
     * @param text given text.
     */
    public void writeToFile(String targetFileName ,String text){

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(targetFileName, MODE_PRIVATE);
            fos.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method reads the text in the target file and returns it in String format.
     * @param v View.
     * @return the text in the target file in String format.
     */
    public String readFromFile(View v){
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String text;

            while ((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * This method was created to navigate to the Help Screen.
     */
    private void navigateToHelpScreen(){
        Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.help_tag), InformationTransferManager.getStringResource(R.string.help_text), false);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.HelpScreenFragment);
    }

    /**
     * This method was created to navigate to the About Screen.
     */
    private void navigateToAboutScreen(){
        Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.about_tag), InformationTransferManager.getStringResource(R.string.about_text), false);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.AboutScreenFragment);
    }

    /**
     * This method creates a dialog box to select the action to be taken for log messages.
     */
    private void showDialogForLogMessages(){
        dialogBuilder.setMessage(InformationTransferManager.getStringResource(R.string.dialog_box_text))
                .setCancelable(false)
                .setPositiveButton(InformationTransferManager.getStringResource(R.string.dialog_box_yes), (dialog, id) -> {
                    InformationTransferManager.clearTotalLogMessages();
                    Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.dialog_box_tag),
                            InformationTransferManager.getStringResource(R.string.dialog_box_message_1), false);
                })
                .setNegativeButton(InformationTransferManager.getStringResource(R.string.dialog_box_no), (dialog, id) -> {
                    dialog.cancel();
                    Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.dialog_box_tag),
                            InformationTransferManager.getStringResource(R.string.dialog_box_message_2), false);
                });
        //Creating dialog box
        AlertDialog alert = dialogBuilder.create();
        alert.setTitle(InformationTransferManager.getStringResource(R.string.dialog_box_title));
        alert.show();
    }
}