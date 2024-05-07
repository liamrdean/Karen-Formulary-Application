package com.karenformulary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.github.barteksc.pdfviewer.PDFView;

public class ActivityG6PD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zscore);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        PDFView pdfView = findViewById(R.id.pdfview);
        try {
            // Define the page numbers you want to display
            int page1 = 19; // Adjusted to your new desired start page
            int page2 = 20; // Adjusted to your new desired start page


            // Load the specified pages
            pdfView.fromAsset("2021KarenFormulary.pdf")
                    .pages(page1 - 1, page2 - 1) // Index starts from 0, so subtract 1
                    .load();
        } catch (Exception e) {
            Log.e("ActivityG6PD", "Error loading PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
