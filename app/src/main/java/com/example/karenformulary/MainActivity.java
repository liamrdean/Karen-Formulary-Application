package com.example.karenformulary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button druginfopageBTN;
    Button zscoreBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        druginfopageBTN = (Button)findViewById(R.id.druginfopageBTN);
        druginfopageBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the Drug info Page
                Intent intent = new Intent(MainActivity.this, DrugInfoPageActivity.class);
                startActivity(intent);

            }
        });


        zscoreBTN = (Button)findViewById(R.id.zscoreBTN);
        zscoreBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the Drug info Page
                Intent intent = new Intent(MainActivity.this, ZscoreActivity.class);
                startActivity(intent);

            }
        });
    }
}