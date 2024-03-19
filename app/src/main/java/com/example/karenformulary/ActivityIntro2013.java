package com.example.karenformulary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.github.barteksc.pdfviewer.PDFView;

public class ActivityIntro2013 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zscore);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PDFView pdfView = findViewById(R.id.pdfview);
        try {
            // Define the page numbers you want to display
            int page1 = 12; // Adjusted to your new desired start page
            int page2 = 13; // Adjusted to your new desired start page
            int page3 = 14; // Adjusted to your new desired start page

            // Load the specified pages
            pdfView.fromAsset("2021KarenFormulary.pdf")
                    .pages(page1 - 1, page2 - 1, page3 - 1) // Index starts from 0, so subtract 1
                    .load();
        } catch (Exception e) {
            Log.e("ActivityIntro2013", "Error loading PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
