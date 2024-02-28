package com.example.karenformulary;

import android.database.Cursor;

import java.util.HashMap;


// This is intended to be a storage class for data that depends on language.
public class DrugInfo {

    // A short identifying char that ids a column as empty
    public static final String nullId = ".";

    // If any aspect should not be included, simply set it to null
    /*
    public final String description;
    public String food;
    public String caution;
    public String dontGive;
    public String sideEffects;
    public String interactions;
    public String pregnancy;
    public String breastFeeding;

    */
    public HashMap<String, String> information;


    public DrugInfo(Cursor cursor, boolean inKaren) {
        information = new HashMap<>();

        // Put each header into the thing with their headers
        for (int i = 0; i < DB_Helper.languageIndependentHeaders.length; i++) {
            String header = DB_Helper.languageIndependentHeaders[i];
            String headerWithLanguage = DB_Helper.addLanguageSuffix(header, inKaren);
            information.put(header, cursor.getString(DB_Helper.getHeaderIndex(headerWithLanguage)));
        }



    }

    /*
    // Must be language independent
    public String getInfo(String header) {
        information.
    }
    */

    @Override
    public String toString() {
        String out = "";
        return out;
    }
}

