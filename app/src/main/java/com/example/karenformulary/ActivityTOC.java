package com.example.karenformulary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class ActivityTOC extends AppCompatActivity {

    // BUTTONS
    Button fullPDF;
    Button intro2021;
    Button intro2013;
    Button generalGuidelines;
    Button drugsInPregnancy;
    Button g6pd;
    Button drugsListGroup;
    Button aboutFerrous;
    Button comboTreatment;
    Button pphDrugs;
    Button aboutAntiRetroviral;
    Button comboAntibiotics;
    Button tbDrugs;
    Button antiseptics;
    Button woundCare;
    Button ivFluids;
    Button malaria;
    Button vaccination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_of_contents);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // FULL PDF BUTTON
        fullPDF = (Button) findViewById(R.id.fullPDF);
        fullPDF.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityFullPDF.class);
                startActivity(intent);
            }
        });

        // INTRO TO 2021 EDITION
        intro2021 = (Button) findViewById(R.id.intro2021);
        intro2021.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityIntro2021.class);
                startActivity(intent);
            }
        });


        // INTRO TO 2013 EDITION
        intro2013 = (Button) findViewById(R.id.intro2013);
        intro2013.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityIntro2013.class);
                startActivity(intent);
            }
        });


        // GENERAL GUIDELINES
        generalGuidelines = (Button) findViewById(R.id.generalGuidelines);
        generalGuidelines.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityGeneralGuidelines.class);
                startActivity(intent);
            }
        });


        // DRUGS IN PREGNANCY
        drugsInPregnancy = (Button) findViewById(R.id.drugsInPregnancy);
        drugsInPregnancy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityDrugsInPregnancy.class);
                startActivity(intent);
            }
        });

        // DRUGS AND G6PD DEFICIENCY
        g6pd = (Button) findViewById(R.id.g6pd);
        g6pd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityG6PD.class);
                startActivity(intent);
            }
        });

        // DRUGS LIST BY GROUP
        drugsListGroup = (Button) findViewById(R.id.drugListGroup);
        drugsListGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityDrugListGroup.class);
                startActivity(intent);
            }
        });

        // ABOUT FERROUS (IRON), VITAMINS AND MULTIVITAMINS
        aboutFerrous = (Button) findViewById(R.id.aboutFerrous);
        aboutFerrous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityAboutFerrous.class);
                startActivity(intent);
            }
        });

        // COMBINATION TREATMENT FOR GENITO-URINARY DISEASES
        comboTreatment = (Button) findViewById(R.id.comboTreatment);
        comboTreatment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityComboTreatment.class);
                startActivity(intent);
            }
        });

        // DRUGS USED FOR POST PARTUM HAEMORRAGE (PPH) and MISSED or INCOMPLETE ABORTION
        pphDrugs = (Button) findViewById(R.id.pphDrugs);
        pphDrugs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityPPHDrugs.class);
                startActivity(intent);
            }
        });

        // ABOUT ANTI-RETROVIRAL DRUGS
        aboutAntiRetroviral = (Button) findViewById(R.id.aboutAntiRetroviral);
        aboutAntiRetroviral.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityAntiRetroviral.class);
                startActivity(intent);
            }
        });

        // USE OF ANTIBIOTICS IN COMBINATION
        comboAntibiotics = (Button) findViewById(R.id.comboAntibiotics);
        comboAntibiotics.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityComboAntibiotics.class);
                startActivity(intent);
            }
        });

        // DRUGS FOR THE TREATMENT OF NEW CASES OF TB
        tbDrugs = (Button) findViewById(R.id.tbDrugs);
        tbDrugs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityTBDrugs.class);
                startActivity(intent);
            }
        });

        // ANTISEPTICS - DISINFECTANTS
        antiseptics = (Button) findViewById(R.id.antiseptics);
        antiseptics.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityAntiseptic.class);
                startActivity(intent);
            }
        });

        // WOUND CARE / ABSCESS CARE
        woundCare = (Button) findViewById(R.id.woundCare);
        woundCare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityWoundCare.class);
                startActivity(intent);
            }
        });

        // IV FLUIDS
        ivFluids = (Button) findViewById(R.id.ivFluids);
        ivFluids.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityIV.class);
                startActivity(intent);
            }
        });

        // MALARIA PROTOCOLS
        malaria = (Button) findViewById(R.id.malaria);
        malaria.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityMalaria.class);
                startActivity(intent);
            }
        });

        // VACCINATION SCHEDULE (Thailand, Burma)
        vaccination = (Button) findViewById(R.id.vaccination);
        vaccination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTOC.this, ActivityVaccination.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
