package com.karenformulary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.github.barteksc.pdfviewer.PDFView;

public class ActivityMalaria extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zscore);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PDFView pdfView = findViewById(R.id.pdfview);
        try {
            // Define the page numbers you want to display for pages 170-202
            int page1 = 170, page2 = 171, page3 = 172, page4 = 173, page5 = 174,
                    page6 = 175, page7 = 176, page8 = 177, page9 = 178, page10 = 179,
                    page11 = 180, page12 = 181, page13 = 182, page14 = 183, page15 = 184,
                    page16 = 185, page17 = 186, page18 = 187, page19 = 188, page20 = 189,
                    page21 = 190, page22 = 191, page23 = 192, page24 = 193, page25 = 194,
                    page26 = 195, page27 = 196, page28 = 197, page29 = 198, page30 = 199,
                    page31 = 200, page32 = 201, page33 = 202;

            // Load the specified pages
            pdfView.fromAsset("2021KarenFormulary.pdf")
                    .pages(
                            page1 - 1, page2 - 1, page3 - 1, page4 - 1, page5 - 1,
                            page6 - 1, page7 - 1, page8 - 1, page9 - 1, page10 - 1,
                            page11 - 1, page12 - 1, page13 - 1, page14 - 1, page15 - 1,
                            page16 - 1, page17 - 1, page18 - 1, page19 - 1, page20 - 1,
                            page21 - 1, page22 - 1, page23 - 1, page24 - 1, page25 - 1,
                            page26 - 1, page27 - 1, page28 - 1, page29 - 1, page30 - 1,
                            page31 - 1, page32 - 1, page33 - 1
                    )
                    .load();
        } catch (Exception e) {
            Log.e("ActivityMalaria", "Error loading PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
