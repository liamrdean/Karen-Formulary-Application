package com.example.karenformulary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.github.barteksc.pdfviewer.PDFView;

public class ActivityTBDrugs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zscore);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PDFView pdfView = findViewById(R.id.pdfview);
        try {
            // Define the page numbers you want to display for pages 150-159
            int page1 = 150; // New desired start page
            int page2 = 151; // Next page
            int page3 = 152; // Next page
            int page4 = 153; // Next page
            int page5 = 154; // Next page
            int page6 = 155; // Next page
            int page7 = 156; // Next page
            int page8 = 157; // Next page
            int page9 = 158; // Next page
            int page10 = 159; // New desired end page

            // Load the specified pages
            pdfView.fromAsset("2021KarenFormulary.pdf")
                    .pages(
                            page1 - 1, page2 - 1, page3 - 1, page4 - 1, page5 - 1,
                            page6 - 1, page7 - 1, page8 - 1, page9 - 1, page10 - 1
                    ) // Index starts from 0, so subtract 1 from each page number
                    .load();
        } catch (Exception e) {
            Log.e("ActivityTBDrugs", "Error loading PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
