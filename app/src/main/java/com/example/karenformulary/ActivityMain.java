package com.example.karenformulary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

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
    static Switch languageSW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        drugsearchBTN = (Button)findViewById(R.id.drugsearchBTN);
        drugsearchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the Drug info Page
                Intent intent = new Intent(ActivityMain.this, SearchPageActivity.class);
                startActivity(intent);

            }
        });

        zscoreBTN = (Button)findViewById(R.id.zscoreBTN);
        zscoreBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the Drug info Page
                Intent intent = new Intent(ActivityMain.this, ActivityZscore.class);
                startActivity(intent);

            }
        });

        viewformularyBTN = (Button)findViewById(R.id.viewformularyBTN);
        viewformularyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the Drug info Page
                Intent intent = new Intent(ActivityMain.this, ActivityTOC.class);
                startActivity(intent);
            }
        });

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
