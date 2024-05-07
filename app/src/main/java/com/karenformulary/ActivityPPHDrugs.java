package com.karenformulary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.github.barteksc.pdfviewer.PDFView;

public class ActivityPPHDrugs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zscore);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        PDFView pdfView = findViewById(R.id.pdfview);
        try {
            // Define the page numbers you want to display
            int page1 = 139; // Adjusted to your desired start page
            int page2 = 140; // Adjusted to your desired second page
            int page3 = 141; // Adjusted to your desired third page
            int page4 = 142; // Adjusted to your desired fourth page
            int page5 = 143; // Adjusted to your desired fifth page

            // Load the specified pages
            pdfView.fromAsset("2021KarenFormulary.pdf")
                    .pages(page1 - 1, page2 - 1, page3 - 1, page4 - 1, page5 - 1) // Index starts from 0, so subtract 1
                    .load();
        } catch (Exception e) {
            Log.e("ActivityPPHDrugs", "Error loading PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
