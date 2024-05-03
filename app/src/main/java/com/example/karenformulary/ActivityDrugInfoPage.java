package com.example.karenformulary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityDrugInfoPage extends AppCompatActivity {

    public static String drugName = "";

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    TextView nameTextView;
    TextView dosageTextView;
    TextView descriptionTextView;
    ImageTextView treatmentTableImageTextView;
    ImageView imageView2;

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
        //treatmentTableImageTextView = findViewById(R.id.imageTextViewTreatmentTable);
        imageView2 = findViewById(R.id.imageView2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // For now hard coded to Dihydrogen Monoxide
        //drugName = "Dihydrogen Monoxide";
        this.postSettingDrugName();
    }


    public String getDrugName() {return drugName;}
    public static void setDrugName(String newName) {
        drugName = newName;
        //postSettingDrugName();
    }

    public void postSettingDrugName() {
        if (drugName == null || drugName.isEmpty()) {
            return;
        }
        expandableListDetail = ExpandableListDataPump.getData(drugName);

        // This is stupid. Very stupid, but it will ensure that the order is always the same so....

        Log.i("ELDS1", expandableListDetail.keySet().toString());
        List<String> expandableListTitleSet = new ArrayList<>(expandableListDetail.keySet());
        Log.i("ELDS2", expandableListTitleSet.toString());

        nameTextView.setText(drugName);

        // Place the dosage into the dosage text view (For now just grabs the first one)
        List<String> details = expandableListDetail.get(DB_Helper.COL_DOSAGE_DISPLAY_STRING);
        if (details != null && details.get(0) != null) {
            dosageTextView.setText(details.get(0));
        } else {
            dosageTextView.setText("");
        }
        expandableListTitleSet.remove(DB_Helper.COL_DOSAGE_DISPLAY_STRING);

        // Place the description into the description text view (For now just grabs the first one)
        details = expandableListDetail.get(DB_Helper.COL_DESCRIPTION_DISPLAY_STRING);
        if (details != null && details.get(0) != null) {
            descriptionTextView.setText(details.get(0));
        } else {
            descriptionTextView.setText("");
        }
        expandableListTitleSet.remove(DB_Helper.COL_DESCRIPTION_DISPLAY_STRING);


        // Bad but will work for now
        Bitmap bitmap = ImageTextView.getImageBitmap("1");
        if (bitmap != null) {
            imageView2.setImageBitmap(bitmap);
        } else {
            imageView2.setImageResource(R.drawable.test2);
        }
        int screen_width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        int width = imageView2.getDrawable().getIntrinsicWidth();
        int height = imageView2.getDrawable().getIntrinsicHeight();
        double scale = (double)(screen_width)/width;
        int final_height = Math.toIntExact(Math.round(scale * height));
        imageView2.getLayoutParams().height = final_height;

        expandableListTitle = new ArrayList<>();
        for (int i = 2; i < DB_Helper.sqlColStrings.size(); i++) {
            Log.i("DispHead", DB_Helper.drugDisplayHeaders.toString());
            String dispHeader = DB_Helper.drugDisplayHeaders.get(DB_Helper.sqlColStrings.get(i));
            if (expandableListTitleSet.contains(dispHeader)) {
                expandableListTitle.add(dispHeader);
            }
        }

        Log.i("ELDS", expandableListTitle.toString());
        // End of stupidity

        expandableListAdapter = new CustomExpandableListAdapter
                (this, expandableListTitle,expandableListDetail);

        expandableListView.setAdapter(expandableListAdapter);

        // TEMP
        // Testing code
        List<DB_DrugModel> models = ActivityMain.dbHelper.getAllDrugs();
        for (DB_DrugModel model : models) {
            Log.i("DEMOload", model.toString());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
