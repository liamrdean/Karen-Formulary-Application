package com.example.karenformulary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /// START DEMO_UI
    Button btn_search, btn_clear, btn_descEn, btn_descKa;
    EditText en_drugId, et_drugName;
    ListView lv_display;
    /// END DEMO_UI

    // The boolean representing the current language selection
    public static boolean isKaren = false;
    public static DB_Helper dbHelper;
    // Since the database will only be updated during initialization, we can store all drugs once
    public static List<DB_DrugModel> allDrugModels;
    public static List<DB_DrugModel> currentDrugModels;
    public static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DB_Helper(this);

        allDrugModels = dbHelper.getAllDrugs();

        int i = 0;
        for (DB_DrugModel model : allDrugModels) {
            Log.i("DB_DEMO", i + " " + model.toStringVerbose());
            i++;
        }


        /// START DEMO_UI
        btn_search = findViewById(R.id.btn_search);
        btn_clear = findViewById(R.id.btn_clear);
        btn_descEn = findViewById(R.id.btn_descEn);
        btn_descKa = findViewById(R.id.btn_descKa);
        en_drugId = findViewById(R.id.en_drugId);
        et_drugName = findViewById(R.id.et_drugName);
        lv_display = findViewById(R.id.lv_display);

        // I am aware that this is cursed, but this is for the buttons in the demo
        // and the compiler would not be quite otherwise so... lets call it "temporary"
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the drug models & ensure that they exist
                Log.i("DB_DEMO", "Search button");
                DB_Helper dbHelper = MainActivity.dbHelper;
                if (dbHelper == null) { return; }

                List<DB_DrugModel> drugModels;
                // Basically a if number: parse number else: parse name and display handles the rest
                try {
                    // Try getting by the id field
                    int id = Integer.parseInt(en_drugId.getText().toString());
                    drugModels= dbHelper.getDrugByID(id);
                    Log.i("DEMO", Boolean.toString(drugModels == null));
                } catch (NumberFormatException e) {
                    // Get the drugs based on input into the name field
                    String nameInput = et_drugName.getText().toString();
                    drugModels = dbHelper.getDrugByName(nameInput);
                }

                display(drugModels);
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DB_DEMO_BTN", "Clear button");
                // Should this be hard coded? no. But this is a demo so who cares
                String defaultDrugNameText = (isKaren)? "Drug name but in karen" : "Drug name";
                et_drugName.setText(defaultDrugNameText, TextView.BufferType.EDITABLE);
                en_drugId.setText("", TextView.BufferType.EDITABLE);
                display(MainActivity.allDrugModels);
            }
        });

        btn_descEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DB_DEMO_BTN", "Show English description");
                MainActivity.isKaren = false;
                display(MainActivity.currentDrugModels);
            }
        });

        btn_descKa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DB_DEMO_BTN", "Show Karen description");
                MainActivity.isKaren = true;
                display(MainActivity.currentDrugModels);
            }
        });

        // Start by displaying everything
        display(allDrugModels);

    }

    // Display the drugs out, using the DB_DrugModel.toString(void) method
    private void display(List<DB_DrugModel> drugModels) {
        int layout = android.R.layout.simple_list_item_1;
        if (drugModels == null || drugModels.size() == 0) {
            // No drugs returned, display an error message
            Log.i("DB_DEMO", "display got no models");

            // This could simply just change it to be the default, but that is boring & no feedback
            String[] errorArray = {(isKaren) ? "No matching drugs but in karen" : "No matching drugs!"};
            List<String> errorList = Arrays.asList(errorArray);
            arrayAdapter = new ArrayAdapter<String>(MainActivity.this, layout, errorList);
            MainActivity.currentDrugModels = null;
        } else {
            // Found drugs, update the list view to show the drugs in drugModels
            arrayAdapter = new ArrayAdapter<DB_DrugModel>(
                    MainActivity.this, layout, drugModels);
            MainActivity.currentDrugModels = drugModels;
        }

        // Actually display the information
        if (lv_display == null || arrayAdapter == null) {
            Log.w("DB_DEMO", "BAD RESULTS");
        }
        lv_display.setAdapter(MainActivity.arrayAdapter);
    }
    /// END DEMO_UI
}