package com.example.karenformulary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrugInfoPageActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String,List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_info_page);
        expandableListView = findViewById(R.id.expandableListView);

        Log.i("DEMO", "PreDetail");
        expandableListDetail = ExpandableListDataPump.getData();

        Log.i("DEMO", "PreTitle");
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());


        Log.i("DEMO", "Adaptor");
        expandableListAdapter = new CustomExpandableListAdapter
                (this,expandableListTitle,expandableListDetail);


        Log.i("DEMO", "SetAdaptor");
        expandableListView.setAdapter(expandableListAdapter);
    }
}