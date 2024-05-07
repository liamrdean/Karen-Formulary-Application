package com.karenformulary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.github.barteksc.pdfviewer.PDFView;

public class ActivityIV extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zscore);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        PDFView pdfView = findViewById(R.id.pdfview);
        try {
            // Define the page numbers you want to display for pages 164-169
            int page1 = 164; // Adjusted to your new desired start page
            int page2 = 165; // Adjusted to your new desired second page
            int page3 = 166; // Adjusted to your new desired third page
            int page4 = 167; // Adjusted to your new desired fourth page
            int page5 = 168; // Adjusted to your new desired fifth page
            int page6 = 169; // Adjusted to your new desired sixth page

            // Load the specified pages
            pdfView.fromAsset("2021KarenFormulary.pdf")
                    .pages(page1 - 1, page2 - 1, page3 - 1, page4 - 1, page5 - 1, page6 - 1) // Index starts from 0, so subtract 1
                    .load();
        } catch (Exception e) {
            Log.e("ActivityIV", "Error loading PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
