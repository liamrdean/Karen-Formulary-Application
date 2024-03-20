package com.example.karenformulary;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityFullPDF extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_pdf);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PDFView pdfView = findViewById(R.id.pdfviewTOC);

        pdfView.fromAsset("2021KarenFormulary.pdf").load();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
