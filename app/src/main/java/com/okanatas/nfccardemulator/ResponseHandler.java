package com.okanatas.nfccardemulator;

/**
 * This class was created to select responses for command APDU.
 * @author Okan Atas,
 * @version 1.0,
 * created on June 30, 2021
 */
public class ResponseHandler {

    private static String selectedInsDescription;

    /**
     * Command APDU - Select Case.
     * @param hexCommandApdu command APDU in hexadecimal format.
     * @return response APDU.
     */
    static byte[] selectCase(String hexCommandApdu){
        byte[] responseApdu = Utils.hexStringToByteArray(ISOProtocol.SW_FILE_NOT_FOUND);
        selectedInsDescription = InformationTransferManager.getStringResource(R.string.ins_select_case);

        for(int i = 0; i < FileHandler.commands.size(); i++){
            if(hexCommandApdu.equals(FileHandler.commands.get(i))){
                responseApdu = Utils.hexStringToByteArray(FileHandler.responses.get(i));
                break;
            }
        }
        return responseApdu;
    }

    /**
     * Command APDU - Read Binary Case.
     * @return response APDU.
     */
    static byte[] readBinaryCase(){
        byte[] responseApdu = Utils.hexStringToByteArray(ISOProtocol.SW_RECORD_NOT_FOUND);
        selectedInsDescription = InformationTransferManager.getStringResource(R.string.ins_read_binary);

        for(int i = 0; i < FileHandler.commands.size(); i++){
            if(ISOProtocol.INS_READ_BINARY.equals(FileHandler.commands.get(i).substring(2,4))){
                responseApdu = Utils.hexStringToByteArray(FileHandler.responses.get(i));
                break;
            }
        }
        return responseApdu;
    }

    /**
     * Command APDU - Get Processing Option Case.
     * @param hexCommandApdu command APDU in hexadecimal format.
     * @return response APDU.
     */
    static byte[] getProcessingOptionCase(String hexCommandApdu){
        byte[] responseApdu = Utils.hexStringToByteArray(ISOProtocol.SW_COMMAND_ABORTED);
        selectedInsDescription = InformationTransferManager.getStringResource(R.string.ins_get_processing_option);

        for(int i = 0; i < FileHandler.commands.size(); i++){
            if(hexCommandApdu.equals(FileHandler.commands.get(i))){
                responseApdu = Utils.hexStringToByteArray(FileHandler.responses.get(i));
                break;
            }
        }
        return responseApdu;
    }

    /**
     * Command APDU - Read Record Case.
     * @param hexCommandApdu command APDU in hexadecimal format.
     * @return response APDU.
     */
    static byte[] readRecordCase(String hexCommandApdu){
        byte[] responseApdu = Utils.hexStringToByteArray(ISOProtocol.SW_RECORD_NOT_FOUND);
        selectedInsDescription = InformationTransferManager.getStringResource(R.string.ins_read_record);

        for(int i = 0; i < FileHandler.commands.size(); i++){
            if(hexCommandApdu.equals(FileHandler.commands.get(i))){
                responseApdu = Utils.hexStringToByteArray(FileHandler.responses.get(i));
                break;
            }
        }
        return responseApdu;
    }

     /**
     * Command APDU - Perform Security Operation Case.
     * @return response APDU.
     */
    static byte[] performSecurityOperationCase(){
        byte[] responseApdu = Utils.hexStringToByteArray(ISOProtocol.SW_COMMAND_ABORTED);
        selectedInsDescription = InformationTransferManager.getStringResource(R.string.ins_perform_security);

        for(int i = 0; i < FileHandler.commands.size(); i++){
            if(ISOProtocol.INS_PERFORM_SECURITY_OPERATION.equals(FileHandler.commands.get(i).substring(2,4))){
                responseApdu = Utils.hexStringToByteArray(FileHandler.responses.get(i));
                break;
            }
        }
        return responseApdu;
    }

    /**
     * Command APDU - Read NDEF Case.
     * @return response APDU.
     */
    static byte[] readNdefCase(){
        byte[] responseApdu = Utils.hexStringToByteArray(ISOProtocol.SW_COMMAND_ABORTED);
        selectedInsDescription = InformationTransferManager.getStringResource(R.string.ins_read_ndef);

        for(int i = 0; i < FileHandler.commands.size(); i++){
            if(ISOProtocol.INS_READ_NDEF.equals(FileHandler.commands.get(i).substring(2,4))){
                responseApdu = Utils.hexStringToByteArray(FileHandler.responses.get(i));
                break;
            }
        }
        return responseApdu;
    }

    /**
     * Command APDU - Write Binary Case.
     * @return response APDU.
     */
    static byte[] writeBinaryCase(){
        byte[] responseApdu = Utils.hexStringToByteArray(ISOProtocol.SW_COMMAND_ABORTED);
        selectedInsDescription = InformationTransferManager.getStringResource(R.string.ins_write_binary);

        for(int i = 0; i < FileHandler.commands.size(); i++){
            if(ISOProtocol.INS_WRITE_BINARY.equals(FileHandler.commands.get(i).substring(2,4))){
                responseApdu = Utils.hexStringToByteArray(FileHandler.responses.get(i));
                break;
            }
        }
        return responseApdu;
    }

    /**
     * Command APDU - Update Binary Case.
     * @return response APDU.
     */
    static byte[] updateBinaryCase(){
        byte[] responseApdu = Utils.hexStringToByteArray(ISOProtocol.SW_COMMAND_ABORTED);
        selectedInsDescription = InformationTransferManager.getStringResource(R.string.ins_update_binary);

        for(int i = 0; i < FileHandler.commands.size(); i++){
            if(ISOProtocol.INS_UPDATE_BINARY.equals(FileHandler.commands.get(i).substring(2,4))){
                responseApdu = Utils.hexStringToByteArray(FileHandler.responses.get(i));
                break;
            }
        }
        return responseApdu;
    }

    /**
     * Command APDU - Generate Application Cryptogram Case.
     * @return response APDU.
     */
    static byte[] generateApplicationCryptogramCase(){
        byte[] responseApdu = Utils.hexStringToByteArray(ISOProtocol.SW_COMMAND_ABORTED);
        selectedInsDescription = InformationTransferManager.getStringResource(R.string.ins_generate_app_cryptogram);

        for(int i = 0; i < FileHandler.commands.size(); i++){
            if(ISOProtocol.INS_GENERATE_APPLICATION_CRYPTOGRAM.equals(FileHandler.commands.get(i).substring(2,4))){
                responseApdu = Utils.hexStringToByteArray(FileHandler.responses.get(i));
                break;
            }
        }
        return responseApdu;
    }

    /**
     * Command APDU - Get Data Case.
     * @return response APDU.
     */
    static byte[] getDataCase(){
        byte[] responseApdu = Utils.hexStringToByteArray(ISOProtocol.SW_COMMAND_ABORTED);
        selectedInsDescription = InformationTransferManager.getStringResource(R.string.ins_get_data);

        for(int i = 0; i < FileHandler.commands.size(); i++){
            if(ISOProtocol.INS_GET_DATA.equals(FileHandler.commands.get(i).substring(2,4))){
                responseApdu = Utils.hexStringToByteArray(FileHandler.responses.get(i));
                break;
            }
        }
        return responseApdu;
    }

    /**
     * To get selected INS description.
     * @return selected INS description.
     */
    static String getSelectedInsDescription(){
        return selectedInsDescription;
    }

    /**
     * To set selected INS description.
     * @param description INS description.
     */
    static void setSelectedInsDescription(String description){
        selectedInsDescription = description;
    }

}
