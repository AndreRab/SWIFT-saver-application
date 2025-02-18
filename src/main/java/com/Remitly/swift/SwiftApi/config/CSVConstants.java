package com.Remitly.swift.SwiftApi.config;

public class CSVConstants {
    public final static String FILE_NAME = "Interns_2025_SWIFT_CODES_Sheet1.csv";

    public final static String ADDRESS = "ADDRESS";
    public final static String BANK_NAME = "NAME";
    public final static String COUNTRY_ISO_2 = "COUNTRY ISO2 CODE";
    public final static String COUNTRY_NAME = "COUNTRY NAME";
    public final static String SWIFT_CODE = "SWIFT CODE";

    public final static String SUCCESS_MESSAGE = "🚀 Init banks download ⏳ ... ✅ Successfully completed! ✔️";
    public final static String ERROR_MESSAGE = "❌ Can't load file with init banks: ";
    public final static String DUPLICATE_MESSAGE = "Duplicate SWIFT code detected: ";

    public final static int MIN_SWIFT_CODE_LENGTH = 8;
    public final static int MAX_SWIFT_CODE_LENGTH = 11;
}
