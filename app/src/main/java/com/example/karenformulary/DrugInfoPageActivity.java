package com.example.karenformulary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrugInfoPageActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    TextView nameTextView;
    TextView dosageTextView;
    TextView allergyWarning;
    TextView descriptionTextView;

    List<String> expandableListTitle;
    HashMap<String,List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_info_page);
        expandableListView = findViewById(R.id.expandableListView);
        nameTextView = findViewById(R.id.txDrugName);
        dosageTextView = findViewById(R.id.txDosage);
        descriptionTextView = findViewById(R.id.txDrugDescription);
        allergyWarning = findViewById(R.id.txAlergyWarning);

        // For now hard coded to Dihydrogen Monoxide
        String drugName = "Dihydrogen Monoxide";
        expandableListDetail = ExpandableListDataPump.getData(drugName);


        //expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        // This is stupid. Very stupid, but it will ensure that the order is always the same so....
        //*
        Log.i("ELDS1", expandableListDetail.keySet().toString());
        List<String> expandableListTitleSet = new ArrayList<>(expandableListDetail.keySet());
        Log.i("ELDS2", expandableListTitleSet.toString());

        nameTextView.setText(drugName);


        List<String> details = expandableListDetail.get(DB_Helper.COL_DOSAGE_DISPLAY_STRING);
        dosageTextView.setText(details.get(0));
        expandableListTitleSet.remove(DB_Helper.COL_DOSAGE_DISPLAY_STRING);
        details = expandableListDetail.get(DB_Helper.COL_DESCRIPTION_DISPLAY_STRING);
        descriptionTextView.setText(details.get(0));
        expandableListTitleSet.remove(DB_Helper.COL_DESCRIPTION_DISPLAY_STRING);

        expandableListTitle = new ArrayList<>();
        for (int i = 2; i < DB_Helper.sqlColStrings.size(); i++) {
            String dispHeader = DB_Helper.drugDisplayHeaders.get(DB_Helper.sqlColStrings.get(i));
            if (expandableListTitleSet.contains(dispHeader)) {
                expandableListTitle.add(dispHeader);
            }
        }

       Log.i("ELDS", expandableListTitle.toString());

        //*/

        // End of stupidity

        expandableListAdapter = new CustomExpandableListAdapter
                (this, expandableListTitle,expandableListDetail);

        expandableListView.setAdapter(expandableListAdapter);

        // TEMP
        // Testing code
        List<DB_DrugModel> models = MainActivity.dbHelper.getAllDrugs();
        for (DB_DrugModel model : models) {
            Log.i("DEMOload", model.toString());
        }
    }
}