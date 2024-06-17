package com.karenformulary;

public class Settings {

    /*
     * defaultToKaren
     *
     * Can be true or false. Repersents if the language should be Karen by default when the
     * app is opened
     */
    public static final boolean defaultToKaren = false;

    /*
     * The name of the csv file used for the drug database.
     * MUST INCLUDE THE FILE EXTENSION .csv
     */
    public static final String DRUG_CSV_FILE = "database.csv";

    // Change version only when you want the to overwrite previous versions.
    public static final int DATABASE_VERSION = 1;
}
