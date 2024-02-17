package com.example.karenformulary;

// This is intended to be a storage class for data that depends on language.
public class DrugInfo {

    // A short identifying char that ids a column as empty
    public static final String nullId = ".";

    // If any aspect should not be included, simply set it to null
    public final String description;
    public String food;
    public String caution;
    public String dontGive;
    public String sideEffects;
    public String interactions;
    public String pregnancy;
    public String breastFeeding;

    public DrugInfo(String description) {
        this.description = description;
        this.food = null;
        this.caution = null;
        this.dontGive = null;
        this.sideEffects = null;
        this.interactions = null;
        this.pregnancy = null;
        this.breastFeeding = null;
    }

    public DrugInfo(String description, String food, String caution, String dontGive, String sideEffects, String interactions, String pregnancy, String breastFeeding) {
        this.description = description;
        this.food = food;
        this.caution = caution;
        this.dontGive = dontGive;
        this.sideEffects = sideEffects;
        this.interactions = interactions;
        this.pregnancy = pregnancy;
        this.breastFeeding = breastFeeding;
    }

    @Override
    public String toString() {
        String out = "";

        DB_Helper.ColNames[] colNames = DB_Helper.ColNames.values();

        if(this.description != null){ out += "  description='" + this.description + "'\n";}
        if(this.food != null){ out += "  food='" + this.food + "'\n";}
        if(this.caution != null){ out += "  caution='" + this.caution + "'\n";}
        if(this.dontGive != null){ out += "dontGive='" + this.dontGive + "'\n";}
        if(this.sideEffects != null){ out += "sideEffects='" + this.sideEffects + "'\n";}
        if(this.interactions != null){ out += "interactions='" + this.interactions + "'\n";}
        if(this.pregnancy != null){ out += "pregnancy='" + this.pregnancy + "'\n";}
        if(this.breastFeeding != null){ out += "food='" + this.breastFeeding + "'\n";}




        return out;
    }
}

