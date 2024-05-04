package com.okanatas.nfccardemulator;

import android.annotation.SuppressLint;

import java.io.File;

/**
 * This class was created to carry information that should be known by all fragments.
 * @author Okan Atas,
 * @version 1.0,
 * created on June 30, 2021
 */
public class InformationTransferManager {

    /** Log Messages */
    private static StringBuilder allLogMessages = new StringBuilder();
    private static StringBuilder communicationLogMessages = new StringBuilder();
    public static StringBuilder cardCommunicationMessages = new StringBuilder();

    private static String selectedFileText = "";
    private static String selectedFileTextCopy = "";
    private static String appFilesDirectory;

    private static int commandSize;
    private static int responseSize;

    /**
     * This method sets the selected file name.
     * @param selectedFile selected file by the user.
     */
    public static void setSelectedFileText(String selectedFile){
        selectedFileText = selectedFile;
    }

    /**
     * This method gets the selected file.
     * @return selected file name.
     */
    public static String getSelectedFileText(){
        return selectedFileText.isEmpty() ? getStringResource(R.string.default_selected_file_text) : selectedFileText;
    }

    /**
     * This method creates a copy of the selected file name for comparison purposes.
     * @param selectedFileCopy copy of selected file by the user.
     */
    public static void setSelectedFileTextCopy(String selectedFileCopy){
        selectedFileTextCopy = selectedFileCopy;
    }

    /**
     * This method gets copy of the selected file name.
     * @return copy of the selected file name.
     */
    public static String getSelectedFileTextCopy(){
        return selectedFileTextCopy;
    }

    /**
     * This method gets the current status of the HCE service.
     * @return the current status of the HCE service.
     */
    public static String getServiceStatus(){
        return MainScreenFragment.isServiceActivated ? getStringResource(R.string.service_started) : getStringResource(R.string.service_stopped);
    }

    /**
     * This method stores the path of the application's files folder.
     * @param directory the path of the application's files folder.
     */
    public static void setAppFilesDirectory(String directory){
        appFilesDirectory = directory;
    }

    /**
     * This method gets the path of the application's files folder.
     * @return the path of the application's files folder.
     */
    public static String getAppFilesDirectory(){
        return appFilesDirectory;
    }

    /**
     * This method gets a list of available files in the application's files folder.
     * @return list of files in File format.
     */
    public static File[] getApplicationFileList(){
        File directory = new File(getAppFilesDirectory() + "");
        return directory.listFiles();
    }

    /**
     * This method appends card dialog messages to StringBuilder.
     * @param message card dialog message.
     */
    public static void appendCardCommunicationMessage(String message){
        cardCommunicationMessages.append(message).append("\n");
    }

    /**
     * This method gets card dialog message (appended Strings).
     * @return card dialog message (all messages in a String).
     */
    public static String getCardCommunicationMessages(){
        return cardCommunicationMessages.toString();
    }

    /**
     * This method sets the command size.
     * @param size command size.
     */
    public static void setCommandSize(int size){
        commandSize = size;
    }

    /**
     * This method gets the command size.
     * @return command size.
     */
    public static int getCommandSize(){
        return commandSize;
    }

    /**
     * This method sets the response size.
     * @param size response size.
     */
    public static void setResponseSize(int size){
        responseSize = size;
    }

    /**
     * This method gets the response size.
     * @return response size.
     */
    public static int getResponseSize(){
        return responseSize;
    }

    /**
     * This method appends all log messages to StringBuilder.
     * @param logMessage each log messages.
     */
    public static void setAllLogMessages(String logMessage){
        InformationTransferManager.allLogMessages.append(logMessage);
    }

    /**
     * This method appends communication log messages to StringBuilder.
     * @param communicationLogMessage each communication log messages.
     */
    public static void setCommunicationLogMessages(String communicationLogMessage){
        communicationLogMessages.append(communicationLogMessage);
    }

    /**
     * This method clears all log messages with different titles.
     */
    public static void clearTotalLogMessages(){
        allLogMessages.setLength(0);
        communicationLogMessages.setLength(0);
    }

    /**
     * This method gets all log messages with different titles.
     * @return all log messages with different titles.
     */
    public static String getTotalLogMessages(){
        return getStringResource(R.string.logs_title_for_all) + "\n\n"
                + allLogMessages.toString()
                + "\n" + getStringResource(R.string.logs_title_for_communication) + "\n\n"
                + (communicationLogMessages.toString().isEmpty() ? getStringResource(R.string.logs_no_communication) : communicationLogMessages.toString());
    }

    /**
     * This method gets String resources
     * @param resource String resource
     * @return value of the resource in String format
     */
    public static String getStringResource(int resource){
        return MainScreenFragment.getInstance().requireContext().getResources().getString(resource);
    }

    /**
     * This method gets Dimension resources
     * @param resource Dimension resource
     * @return value of the resource in float format
     */
    public static float getDimenResource(int resource){
        return MainScreenFragment.getInstance().requireContext().getResources().getDimension(resource);
    }
}
