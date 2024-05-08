package com.karenformulary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityDrugInfoPage extends AppCompatActivity {

    public static String drugName = "";
    public static ActivityDrugInfoPage currentPage;

    static ExpandableListView expandableListView;
    static ExpandableListAdapter expandableListAdapter;
    TextView nameTextView;
    static TextView dosageTextView;
    static TextView descriptionTextView;
    LinearLayout imagesLinearLayout;
    Switch languageSW;

    static List<String> expandableListTitle_EN;
    static HashMap<String,List<String>> expandableListDetail_EN;
    static List<String> expandableListTitle_KA;
    static HashMap<String,List<String>> expandableListDetail_KA;
    static List<String> expandableListTitleSet_EN;
    static List<String> expandableListTitleSet_KA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_info_page);
        currentPage = this;

        expandableListView = findViewById(R.id.expandableListView);
        nameTextView = findViewById(R.id.txDrugName);
        dosageTextView = findViewById(R.id.txDosage);
        descriptionTextView = findViewById(R.id.txDrugDescription);
        //treatmentTableImageTextView = findViewById(R.id.imageTextViewTreatmentTable);
        imagesLinearLayout = findViewById(R.id.imagesLinearLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        languageSW = (Switch)findViewById(R.id.langaugeSW);
        // Always match the current language setting
        languageSW.setChecked(ActivityMain.isKaren);
        languageSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ActivityMain.languageSwitchChangeHandler(isChecked);
            }
        });
        // Always match the switch's state
        ActivityMain.languageSwitchChangeHandler(languageSW.isChecked());

        this.postSettingDrugName();
    }


    public String getDrugName() {return drugName;}
    public static void setDrugName(String newName) {
        drugName = newName;
        //postSettingDrugName();
    }

    // Force the expandable list to update by making the first group close/open then open/close
    // back into its original state, thus causing no openings or closing but forcing it open
    private void updateExapndable() {
        if (expandableListView == null) {
            return;
        }
        if (expandableListView.getExpandableListAdapter() == null) {
            return;
        }

        // Force a reload of the expandable list
        if (expandableListView != null && expandableListAdapter.getGroupCount() > 0) {
            boolean wasExpanded =  expandableListView.isGroupExpanded(0);
            if (wasExpanded) {
                expandableListView.collapseGroup(0);
            } else {
                expandableListView.expandGroup(0);
            }

            if (wasExpanded) {
                expandableListView.expandGroup(0);
            } else {
                expandableListView.collapseGroup(0);
            }
        }
    }

    public static void onLanguageChange() {
        if (currentPage == null) {
            Log.w("DRUG INFO", "Current page is null");
            return;
        }
        Log.i("DRUG INFO", "Current page not null, reloading");
        currentPage.updateDosage();
        currentPage.updateDescription();
        currentPage.updateExapndable();
    }

    public static void updateDosage() {
        // Place the dosage into the dosage text view (For now just grabs the first one)
        List<String> details = null;
        if (ActivityMain.isKaren && expandableListDetail_KA != null) {
            details = expandableListDetail_KA.get(DB_Helper.COL_DOSAGE_DISPLAY_STRING);
        } else if (expandableListDetail_EN != null) {
            details = expandableListDetail_EN.get(DB_Helper.COL_DOSAGE_DISPLAY_STRING);
        }

        if (details != null) {
            // If there is a dosage, add it, else empty string
            String dosageText = (details != null && details.get(0) != null) ? details.get(0) : "";
            dosageTextView.setText(dosageText);
        } else {
            dosageTextView.setText("");
        }
    }

    public static void updateDescription() {
        // Get the description thing col 0
        List<String> details = null;
        if (ActivityMain.isKaren && expandableListDetail_KA != null) {
            details = expandableListDetail_KA.get(DB_Helper.COL_DESCRIPTION_DISPLAY_STRING);
        } else if (expandableListDetail_EN != null) {
            details = expandableListDetail_EN.get(DB_Helper.COL_DESCRIPTION_DISPLAY_STRING);
        }

        if (details != null) {
            String descriptionText = (details != null && details.get(0) != null) ? details.get(0) : "";
            descriptionTextView.setText(descriptionText);
        } else {
            descriptionTextView.setText("");
        }
    }

    private void setBitmaps() {
        // Add all treatment tables to the thing
        List<Bitmap> bitmaps = ImageTextView.getImageBitmaps();
        int screen_width = Resources.getSystem().getDisplayMetrics().widthPixels;
        if (bitmaps != null) {
            for (Bitmap bitmap : bitmaps) {
                Log.i("bm", bitmap.toString());

                // Create an image view
                ImageView imageView = new ImageView(this);
                // Set the imageview bitmap
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.drawable.test2);
                }
                int width = imageView.getDrawable().getIntrinsicWidth();
                int height = imageView.getDrawable().getIntrinsicHeight();
                double scale = (double) (screen_width) / width;
                int final_height = Math.toIntExact(Math.round(scale * height));

                // Add to the layout
                imagesLinearLayout.addView(imageView);
                imageView.getLayoutParams().height = final_height;
            }
        }
    }

    public void postSettingDrugName() {
        if (drugName == null || drugName.isEmpty()) {
            return;
        }


        // Load the data for each language
        expandableListDetail_EN = ExpandableListDataPump.getData(drugName, false);
        expandableListDetail_KA = ExpandableListDataPump.getData(drugName, true);

        expandableListTitleSet_EN = new ArrayList<>(expandableListDetail_EN.keySet());
        expandableListTitleSet_KA = new ArrayList<>(expandableListDetail_KA.keySet());


        // Set the names of the static texts
        nameTextView.setText(drugName);
        updateDosage();
        updateDescription();
        setBitmaps();

        // Don't want to display the dosage or description in the list
        expandableListTitleSet_EN.remove(DB_Helper.COL_DOSAGE_DISPLAY_STRING);
        expandableListTitleSet_KA.remove(DB_Helper.COL_DOSAGE_DISPLAY_STRING);

        expandableListTitleSet_EN.remove(DB_Helper.COL_DESCRIPTION_DISPLAY_STRING);
        expandableListTitleSet_KA.remove(DB_Helper.COL_DESCRIPTION_DISPLAY_STRING);

        // This is stupid. Very stupid, but it will ensure that the order is always the same so....
        expandableListTitle_EN = new ArrayList<>();
        expandableListTitle_KA = new ArrayList<>();
        Log.i("DispHead", DB_Helper.drugDisplayHeaders.toString());

        for (int i = 2; i < DB_Helper.sqlColStrings.size(); i++) {
            Log.i("DispHeads", i + " " + DB_Helper.sqlColStrings.get(i));

            String dispHeader = DB_Helper.drugDisplayHeaders.get(DB_Helper.sqlColStrings.get(i));

            if (expandableListTitleSet_EN.contains(dispHeader)) {
                Log.i("ljasf en", dispHeader);
                expandableListTitle_EN.add(dispHeader);
            } else if (expandableListTitleSet_KA.contains(dispHeader)) {
                Log.i("ljasf ka", dispHeader);
                expandableListTitle_KA.add(dispHeader);
            } else {
                Log.i("Unwanted", dispHeader);
            }
        }
        // End of stupidity

        Log.i("ELDS", expandableListTitle_EN.toString());

        expandableListAdapter = new CustomExpandableListAdapter
                (this, expandableListTitle_EN, expandableListDetail_EN,
                        expandableListTitle_KA, expandableListDetail_KA);

        expandableListView.setAdapter(expandableListAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
