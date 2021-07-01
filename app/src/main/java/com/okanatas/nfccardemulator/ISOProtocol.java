package com.okanatas.nfccardemulator;

/**
 * This class represents ISO/IEC 7816 Standards
 * @author Okan Atas,
 * @version 1.0,
 * created on June 30, 2021
 */
public class ISOProtocol {
    /* APDU RULE(S) */
    static final int MIN_APDU_LENGTH = 10;

    /* COMMAND INSTRUCTIONS */
    static final String INS_SELECT = "A4";
    static final String INS_READ_BINARY = "B0";
    static final String INS_READ_RECORD = "B2";
    static final String INS_READ_NDEF = "C0";
    static final String INS_WRITE_BINARY = "D0";
    static final String INS_UPDATE_BINARY = "D6";
    static final String INS_GET_PROCESSING_OPTIONS = "A8";
    static final String INS_PERFORM_SECURITY_OPERATION = "2A";
    static final String INS_GENERATE_APPLICATION_CRYPTOGRAM = "AE";
    static final String INS_GET_DATA = "CA";

    /* APDU RESPONSES */
    static final String SW_FILE_NOT_FOUND = "6A82";
    static final String SW_FUNC_NOT_SUPPORTED = "6A81";
    static final String SW_INCORRECT_P1_OR_P2 = "6A86";
    static final String SW_INS_NOT_SUPPORTED_OR_INVALID = "6D00";
    static final String SW_COMMAND_SUCCESSFUL = "9000";
    static final String SW_RECORD_NOT_FOUND = "6A83";
    static final String SW_INCORRECT_DATA_PARAMETERS = "6A80";
    static final String SW_WRONG_LENGTH = "6700";
    static final String SW_WRONG_P1_AND_OR_P2 = "6B00";
    static final String SW_COMMAND_ABORTED = "6F00";
    static final String SW_CLASS_NOT_SUPPORTED= "6E00";
}
