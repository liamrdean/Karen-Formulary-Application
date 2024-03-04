package com.example.karenformulary;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;


public class ZscoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zscore);

        PDFView pdfView = findViewById(R.id.pdfview);
        try {
            // Get the start and end page numbers you want to display
            int page1 = 205; // Change this to your desired start page
            int page2 = 206; // Change this to your desired start page
            int page3 = 207; // Change this to your desired start page
            int page4 = 208; // Change this to your desired start page
            int page5 = 209; // Change this to your desired start page
            int page6 = 210; // Change this to your desired start page
            int page7 = 211; // Change this to your desired start page
            int page8 = 212; // Change this to your desired start page
            int page9 = 213; // Change this to your desired start page
            int page10 = 214; // Change this to your desired start page
            int page11 = 215; // Change this to your desired start page
            int page12 = 216; // Change this to your desired start page
            int page13 = 217; // Change this to your desired start page
            int page14 = 218; // Change this to your desired start page

            // Load specific pages or a range of pages
            pdfView.fromAsset("2021KarenFormulary.pdf")
                    .pages(page1 - 1, page2 - 1, page3 - 1, page4 - 1, page5 - 1, page6 - 1, page7 - 1, page8 - 1,
                            page9 - 1, page10 - 1, page11 - 1, page12 - 1, page13 - 1, page14 - 1) // Index starts from 0, so subtract 1
                    .load();
        } catch (Exception e) {
            Log.e("ZscoreActivity", "Error loading PDF: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
