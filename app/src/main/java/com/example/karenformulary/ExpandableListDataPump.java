package com.example.karenformulary;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static HashMap<String , List<String>> getData(String drugName) {

        HashMap<String, List<String>> expandableListDetail = new HashMap<>();

        // Procedural way of doing this
        // For each header, add stuff as a list of strings returned from the drug model, if there is nothing else, do not deal with it
        // Grab the drugs
        List<DB_DrugModel> drugModels = ActivityMain.dbHelper.getDrugsByName(drugName);

        if (drugModels == null || drugModels.size() == 0) {
            Log.w("ListDataPump", "Drug model list is null or has no models!");
        }

        if (drugModels.size() != 1) {
            Log.w("ListDataPump", "Drug model does not have just 1 model, assuming 1st!");
        }

        // Assume the first drug model
        DB_DrugModel model = drugModels.get(0);
        String[] columnsInModel = (String[]) model.getDataFields().toArray(new String[0]);
        Arrays.sort(columnsInModel);
        Log.i("ELDP", model.toString());
        Log.i("ELDP", Arrays.toString(columnsInModel));

        for (String column : columnsInModel) {
            Log.i("ELDP", column);
            // Get the display name of the column / section / header whatever its called
            String columnWithLanguage = DB_Helper.addCurrentLanguageSuffix(column);
            String displayName = DB_Helper.drugDisplayHeaders.get(columnWithLanguage);

            // Get the contents of the column / section / header whatever its called
            List<String> modelData = model.getData(column);


            Log.i("ELDSinsert", column + " " + columnWithLanguage + " " + displayName + " " + modelData.toString());
            expandableListDetail.put(displayName, modelData);

        }

        return expandableListDetail;
    }
}