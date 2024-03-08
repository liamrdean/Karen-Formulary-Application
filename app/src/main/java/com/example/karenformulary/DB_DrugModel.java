package com.example.karenformulary;

/*
 * This is the java file that models the SQLite database structure for the drugs
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// EN is short for English, KA is short for Karen
public class DB_DrugModel {
    // For when we have not manually assigned an id
    public static final int blankId = -1;

    private int drugId;
    private String drugName;

    /*
    private DrugInfo infoEN;
    private DrugInfo infoKA;
    */
    private HashMap<String, String> tempnameinfoEN;
    private HashMap<String, String> tempnameinfoKA;

    // Constructors
    public DB_DrugModel() {
    }

    public DB_DrugModel(String drugName) {
        this.drugId = blankId;
        this.drugName = drugName;
    }

    public DB_DrugModel(int drugId, String drugName, HashMap<String, String> infoEn, HashMap<String, String> infoKa) {
        this.drugId = drugId;
        this.drugName = drugName;
        this.tempnameinfoEN = infoEn;
        this.tempnameinfoKA = infoKa;
    }

    /*
    public DB_DrugModel(int drugId, String drugName, DrugInfo infoEN, DrugInfo infoKA) {
        this.drugId = drugId;
        this.drugName = drugName;
        this.infoEN = infoEN;
        this.infoKA = infoKA;
    }
    */

    // This is what gets called by the array list generator thing, so deal with it manually
    @Override
    public String toString() {
        // English
        return  "id = " + drugId + "\n" +
                "name = '" + drugName + "'\n" +
                "EN NAMES \n" + tempnameinfoEN.toString() + "\n" +
                "KA NAMES \n" + tempnameinfoKA.toString() + "\n";

//                this.getInfo(MainActivity.isKaren).toString();

        /* Legacy/default
        return "DB_DrugModel{" +
                "drugId=" + drugId +
                ", drugName='" + drugName + '\'' +
                ", description_English='" + description_English + '\'' +
                ", description_Karen='" + description_Karen + '\'' +
                '}';
         */
    }

    // Just toString but new lines between items
    /*
    public String toStringVerbose() {
        String out = "DB_DrugModel{\n" +
                "  drugId=" + drugId + '\n' +
                "  drugName='" + drugName + "\'\n"
                +"\nEN:\n"
                + infoEN.toString()
                +"\nKA:\n"
                + infoKA.toString();
        out += '}';
        return out;
    }
    */

    // Getters and setters

    /*
    public DrugInfo getInfo(boolean inKaren) {
        return (inKaren) ? infoKA : infoEN;
    }
    */

    public List<String> getData(String data) {
        if (data.equals(DB_Helper.COL_NAME_STRING)) {
            // They are just asking for the name of this drug, return it.
            List<String> output = new ArrayList<>();
            output.add(this.drugName);
            return output;
        }






        return null;
    }

}
