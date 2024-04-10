package com.example.karenformulary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                this.activityMain = this;


        Log.i("jklfdsa", "Getting resources");
        Resources resources = this.getResources();
        assetManager = resources.getAssets();
        dbHelper = new DB_Helper(this);
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
    }
}
