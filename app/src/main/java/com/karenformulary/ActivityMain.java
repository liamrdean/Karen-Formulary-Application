package com.karenformulary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class ActivityMain extends AppCompatActivity {

    Button button3;
    // This controls if the data is in karen or not
    public static boolean isKaren = false;
    public static DB_Helper dbHelper;
    public static ActivityMain activityMain;
    public static AssetManager assetManager;

    Button drugsearchBTN;
    Button zscoreBTN;
    Button viewformularyBTN;
    Button drugsInPregnancy;
    static Switch languageSW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        this.activityMain = this;


        Log.i("jklfdsa", "Getting resources");
        Resources resources = this.getResources();
        assetManager = resources.getAssets();
        dbHelper = new DB_Helper(this);

        if (!dbHelper.isInitalized) {
            Log.i("DB_HELPER", "Attempting init from main");
            dbHelper.init();

            if (!dbHelper.isInitalized) {
                Log.e("KaForm INIT", "DB_Helper failed to initalize");
            }
        }

        // Force dbHelper to call onCreate or onUpgrade
        dbHelper.getWritableDatabase().close();


        // DRUG SEARCH BUTTON
        drugsearchBTN = (Button)findViewById(R.id.drugsearchBTN);
        drugsearchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the Drug info Page
                Intent intent = new Intent(ActivityMain.this, SearchPageActivity.class);
                startActivity(intent);

            }
        });

        // ZSCORE BUTTON
        zscoreBTN = (Button)findViewById(R.id.zscoreBTN);
        zscoreBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the Drug info Page
                Intent intent = new Intent(ActivityMain.this, ActivityZscore.class);
                startActivity(intent);

            }
        });


        // Table of Contents Button
        viewformularyBTN = (Button)findViewById(R.id.viewformularyBTN);
        viewformularyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the Drug info Page
                Intent intent = new Intent(ActivityMain.this, ActivityTOC.class);
                startActivity(intent);
            }
        });


        // DRUGS IN PREGNANCY BUTTON
        drugsInPregnancy = (Button) findViewById(R.id.drugsInPregnancy);
        drugsInPregnancy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMain.this, ActivityDrugsInPregnancy.class);
                startActivity(intent);
            }
        });


        // English Karen Switch Toggle
        languageSW = (Switch)findViewById(R.id.langaugeSW);
        languageSW.setChecked(isKaren);
        languageSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                languageSwitchChangeHandler(isChecked);
            }
        });
        // Always match the switch's state
        languageSwitchChangeHandler(languageSW.isChecked());

        TextView tv = (TextView) findViewById(R.id.versionNum);
        tv.setText("Version : " + BuildConfig.VERSION_NAME);

    }

    // Though simple, wanted to abstract so that this can be called anywhere and always have same
    // effect
    // Should be called whenever language needs changing. The argument is the new value of isKaren
    public static void languageSwitchChangeHandler(boolean isChecked) {
        if (isChecked) {
            Log.i("SWITCH TEST", "CHECKED");
        } else {
            Log.i("SWITCH TEST", "UNCHECKED");
        }

        ActivityMain.isKaren = isChecked;
        if (languageSW != null) {
            languageSW.setChecked(isKaren);
        }
        ActivityDrugInfoPage.onLanguageChange();
    }
}
