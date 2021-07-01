package com.okanatas.nfccardemulator;

import java.util.ArrayList;

/**
 * This class was created to detect apdu commands and apdu responses in txt files uploaded by the user.
 * @author Okan Atas,
 * @version 1.0,
 * created on June 30, 2021
 */
public class FileHandler {

    /** This ArrayList holds the commands of the file currently in use. */
    public static ArrayList<String> commands = new ArrayList<>();

    /** This ArrayList holds the responses of the file currently in use. */
    public static ArrayList<String> responses = new ArrayList<>();

    /** Command keyword for the txt file */
    private static final String COMMAND_KEYWORD = InformationTransferManager.getStringResource(R.string.c_apdu_keyword);
    /** Response keyword for the txt file */
    private static final String RESPONSE_KEYWORD = InformationTransferManager.getStringResource(R.string.r_apdu_keyword);

    /**
     * This method was created to save commands and responses to the ArrayList.
     * @param fileContentInText file content in String format.
     */
    public static void setCommandsAndResponses(String fileContentInText){
        // clear ArrayList elements for a new file initialization
        commands.clear();
        responses.clear();

        // remove all white spaces for organization purpose
        fileContentInText = fileContentInText.replaceAll("\\s","");

        // send the keywords for searching commands and responses
        fileContentInText = searchAndOrganize(fileContentInText, COMMAND_KEYWORD, RESPONSE_KEYWORD);

        // store the organized content line by line
        String[] lines = fileContentInText.split("\\r?\\n");

        // determine which line is command and which is response and store it to ArrayList.
        for (String line : lines) {
            String[] parsedLine = line.split(":");

            if (parsedLine[0].equalsIgnoreCase(COMMAND_KEYWORD)) {
                if((parsedLine[1].length() % 2 == 0) || (parsedLine[1].length() >= ISOProtocol.MIN_APDU_LENGTH)){
                    commands.add(parsedLine[1]);
                    Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.command_from_file_text), parsedLine[1], false);
                }else{
                    Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.command_from_file_text), InformationTransferManager.getStringResource(R.string.invalid_message_1), false);
                }
            }

            if (parsedLine[0].equalsIgnoreCase(RESPONSE_KEYWORD)) {
                if((parsedLine[1].length() % 2) == 0){
                    responses.add(parsedLine[1]);
                    Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.response_from_file_text), parsedLine[1], false);
                }else {
                    Utils.showLogDMessage(InformationTransferManager.getStringResource(R.string.response_from_file_text), InformationTransferManager.getStringResource(R.string.invalid_message_2), false);
                }
            }
        }
    }

    /**
     *
     * @param fileContentInText file content in String format.
     * @param commandKeyword command keyword for the txt file.
     * @param responseKeyword response keyword for the txt file.
     * @return organized content.
     */
    public static String searchAndOrganize(String fileContentInText, String commandKeyword, String responseKeyword)
    {
        StringBuilder organizedContent = new StringBuilder();
        ArrayList<Integer> indexes = new ArrayList<>();
        boolean isCharC = true;
        char searchFor;

        // search for command and response keywords and set their index into indexes ArrayList.
        for (int i = 0; i < fileContentInText.length(); i++){
            searchFor = isCharC ? Character.toUpperCase(commandKeyword.charAt(0)) : Character.toUpperCase(responseKeyword.charAt(0));
            if ((Character.toUpperCase(fileContentInText.charAt(i)) == searchFor) && ( i + (isCharC ? commandKeyword.length() : responseKeyword.length()) <= fileContentInText.length())){
                if(fileContentInText.substring(i, i + (isCharC ? commandKeyword.length() : responseKeyword.length())).equalsIgnoreCase(isCharC ? commandKeyword : responseKeyword)){
                    indexes.add(i);
                    isCharC = !isCharC;
                }
            }
        }

        // write each command and response to StringBuilder on separate lines.
        for(int i = 0; i < indexes.size(); i++){
            organizedContent.append(fileContentInText, indexes.get(i), ((i+1 < indexes.size()) ? indexes.get(i+1) : fileContentInText.length())).append("\n");
        }

        return organizedContent.toString();
    }

}
