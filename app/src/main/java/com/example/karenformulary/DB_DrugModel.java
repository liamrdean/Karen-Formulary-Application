package com.example.karenformulary;

/*
 * This is the java file that models the SQLite database structure for the drugs
 */

// EN is short for English, KA is short for Karen
public class DB_DrugModel {
    // For when we have not manually assigned an id
    public static final int blankId = -1;

    private int drugId;
    private String drugName;

    private DrugInfo infoEN;
    private DrugInfo infoKA;

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
        this.infoEN = new DrugInfo(description_English);
        this.infoKA = new DrugInfo(description_Karen);
    }

    public DB_DrugModel(int drugId, String drugName, String description_English,
                        String description_Karen) {
        this.drugId = drugId;
        this.drugName = drugName;
        this.infoEN = new DrugInfo(description_English);
        this.infoKA = new DrugInfo(description_Karen);
    }

    public DB_DrugModel(int drugId, String drugName, DrugInfo infoEN, DrugInfo infoKA) {
        this.drugId = drugId;
        this.drugName = drugName;
        this.infoEN = infoEN;
        this.infoKA = infoKA;
    }

    // This is what gets called by the array list generator thing, so deal with it manually
    @Override
    public String toString() {
        // English
        return  "id = " + drugId + "\n" +
                "name = '" + drugName + "'\n" +
                this.getInfo(MainActivity.isKaren).toString();

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

    // Return if the description, langauge dependent
    public String getDescription(boolean inKaren) {
        if (inKaren) {
            return infoKA.description;
        } else {
            return infoEN.description;
        }
    }

    public DrugInfo getInfo(boolean inKaren) {
        return (inKaren) ? infoKA : infoEN;
    }

}
