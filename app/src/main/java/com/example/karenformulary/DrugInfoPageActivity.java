package com.example.karenformulary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DrugInfoPageActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    TextView nameTextView;
    TextView dosageTextView;
    TextView allergyWarning;

    List<String> expandableListTitle;
    HashMap<String,List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_info_page);
        expandableListView = findViewById(R.id.expandableListView);
        nameTextView = findViewById(R.id.txDrugName);
        dosageTextView = findViewById(R.id.txDosage);
        allergyWarning = findViewById(R.id.txAlergyWarning);

        // For now hard coded to Dihydrogen Monoxide
        String drugName = "Dihydrogen Monoxide";
        expandableListDetail = ExpandableListDataPump.getData(drugName);

//        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        // This is stupid very stupid, but it will ensure that the order is always the same so....



        expandableListAdapter = new CustomExpandableListAdapter
                (this,expandableListTitle,expandableListDetail);

        expandableListView.setAdapter(expandableListAdapter);

        // TEMP
        // Testing code
        List<DB_DrugModel> models = MainActivity.dbHelper.getAllDrugs();
        for (DB_DrugModel model : models) {
            Log.i("DEMOload", model.toString());
        }
    }
}