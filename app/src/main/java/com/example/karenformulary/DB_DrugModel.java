package com.example.karenformulary;

/*
 * This is the java file that models the SQLite database structure for the drugs
 */

public class DB_DrugModel {
    // For when we have not manually assigned an id
    public static final int blankId = -1;

    private int drugId;
    private String drugName;
    private String description_English;
    private String description_Karen;

    // Constructors
    public DB_DrugModel() {
    }

    public DB_DrugModel(String drugName) {
        this.drugId = blankId;
        this.drugName = drugName;
    }

    public DB_DrugModel(int drugId, String drugName) {
        this.drugId = drugId;
        this.drugName = drugName;
    }

    public DB_DrugModel(String drugName, String description_English, String description_Karen) {
        this.drugId = blankId;
        this.drugName = drugName;
        this.description_English = description_English;
        this.description_Karen = description_Karen;
    }

    public DB_DrugModel(int drugId, String drugName, String description_English,
                        String description_Karen) {
        this.drugId = drugId;
        this.drugName = drugName;
        this.description_English = description_English;
        this.description_Karen = description_Karen;
    }

    // This is what gets called by the array list generator thing, so deal with it manually
    @Override
    public String toString() {
        // English
        return  "id = " + drugId + "\n" +
                "name = '" + drugName + "'\n" +
                "description = '" + getDescription(MainActivity.isKaren);

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
    public String toStringVerbose() {
        return "DB_DrugModel{\n" +
                "  drugId=" + drugId + '\n' +
                "  drugName='" + drugName + "\'\n" +
                "  description_English='" + description_English + "\'\n" +
                "  description_Karen='" + description_Karen + "\'\n" +
                '}';
    }

    // Getters and setters
    public int getDrugId() {
        return drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDescription_English() {
        return description_English;
    }

    public void setDescription_English(String description_English) {
        this.description_English = description_English;
    }

    public String getDescription_Karen() {
        return description_Karen;
    }

    public void setDescription_Karen(String description_Karen) {
        this.description_Karen = description_Karen;
    }

    // Return if the description, langauge dependent
    public String getDescription(boolean inKaren) {
        if (inKaren) {
            return getDescription_Karen();
        } else {
            return getDescription_English();
        }
    }
}
