package com.okanatas.nfccardemulator;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * The Utils Class
 * @author Okan Atas,
 * @version 1.0,
 * created on June 30, 2021
 */
public class Utils {
    private static final String HEX_CHARS = "0123456789ABCDEF";
    private static final char[] HEX_CHARS_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * This method converts Hexadecimal String to byte array.
     * @param data hexadecimal String.
     * @return byte array.
     */
    static byte[] hexStringToByteArray(String data){
        byte[] result = new byte[data.length() / 2];

        for(int i = 0; i < data.length(); i += 2){
            int firstIndex = HEX_CHARS.indexOf(data.charAt(i));
            int secondIndex = HEX_CHARS.indexOf(data.charAt(i+1));

            int octet = ((firstIndex << 4) | secondIndex);
            result[i >> 1] = (byte)(octet);
        }
        return result;
    }

    /**
     * This method converts byte array to Hexadecimal String.
     * @param bytes byte array.
     * @return hexadecimal String.
     */
    static String toHexString(byte[] bytes){
        StringBuilder result = new StringBuilder();

        for(byte b : bytes){
            int firstIndex = (((int)b) & 0xF0) >>> 4;
            int secondIndex = ((int)b & 0x0F);

            result.append(HEX_CHARS_ARRAY[firstIndex]);
            result.append(HEX_CHARS_ARRAY[secondIndex]);
        }
        return result.toString();
    }

    /**
     * This method converts input stream to String.
     * @param inputStream input stream.
     * @return String.
     */
    static String inputStreamToString(InputStream inputStream){
        String text = null;
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            text = scanner.useDelimiter("\\A").next();
        }
        return text;
    }

    /**
     * This method deletes the selected file from the given directory.
     * @param directory given directory.
     * @param selectedFile file to be deleted.
     */
    static void deleteFile(String directory, String selectedFile){
        File file = new File(directory + "/" + selectedFile);
        boolean deleted = file.delete();
    }

    /**
     * This method displays Log.d messages for debugging purposes.
     * It also organizes the communication messages.
     * @param TAG TAG.
     * @param message message to show.
     * @param isCommunicationLog boolean to check if it is communication log.
     */
    static void showLogDMessage(String TAG, String message, boolean isCommunicationLog){
        if(TAG.length() > 23){
            TAG = TAG.substring(0,23);
        }
        Log.d(TAG, message);

        if(isCommunicationLog){
            if (!InformationTransferManager.getSelectedFileTextCopy().equals(InformationTransferManager.getSelectedFileText())) {
                InformationTransferManager.setSelectedFileTextCopy(InformationTransferManager.getSelectedFileText());
                InformationTransferManager.setCommunicationLogMessages("\n" + InformationTransferManager.getStringResource(R.string.file_name_tag)
                        + " " + InformationTransferManager.getSelectedFileTextCopy() + "\n");
            }
            InformationTransferManager.setCommunicationLogMessages(TAG + " : " + message + "\n");
        }
        InformationTransferManager.setAllLogMessages(TAG + " : " + message + "\n");
    }

    /**
     * This method displays a long length Snack Bar message.
     */
    static void showSnackBarLong(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * This method displays a short length Snack Bar message.
     */
    static void showSnackBarShort(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * This method displays a long length Toast message.
     */
    static void showToastLong(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * This method displays a short length Toast message.
     */
    static void showToastShort(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
