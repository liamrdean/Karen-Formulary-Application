package com.example.karenformulary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button3;
    // This controls if the data is in karen or not
    public static boolean isKaren = true;
    public static DB_Helper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DB_Helper(this);

        button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the Drug info Page
                Intent intent = new Intent(MainActivity.this, DrugInfoPageActivity.class);
                startActivity(intent);

            }
        });
    }
}