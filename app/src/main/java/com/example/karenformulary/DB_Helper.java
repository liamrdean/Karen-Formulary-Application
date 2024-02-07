package com.example.karenformulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DB_Helper extends SQLiteOpenHelper {

    //public static final String TABLE_DRUG_TO_ID = "TABLE_DRUG_TO_ID";
    public static final String TABLE_ID_TO_DRUG = "TABLE_ID_TO_DRUG";
    public static final String COL_DRUG_ID      = "DRUG_ID";
    public static final String COL_DRUG_NAME    = "DRUG_NAME";
    public static final String COL_DESC_ENGLISH = "DESC_ENGLISH";
    public static final String COL_DESC_KAREN   = "DESC_KAREN";


    public DB_Helper(@Nullable Context context) {
        super(context, "drugDB", null, 1);
    }

    // This is called the first time a database is accessed. There should only be code to create a new database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table (ID, DRUG_NAME)
        String createTableStatement = "CREATE TABLE " + TABLE_ID_TO_DRUG + " (" + COL_DRUG_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_DRUG_NAME + " TEXT, "
                + COL_DESC_ENGLISH + " TEXT, " + COL_DESC_KAREN + " TEXT);";
        db.execSQL(createTableStatement);

        // Now create the Name to ID table
        //createTableStatement = "CREATE TABLE " + DRUG_TO_ID_TABLE + " (" + COL_DRUG_NAME +
        //        " TEXT PRIMARY KEY, " + COL_DRUG_ID + " INTEGER);";

        // Initialize the stuff into the DB
        addAllDrugs(db);
    }

    /*
     * Called when the version number of the database change
     * This technically should only be called when we make changes to update, but we can just
     * nuke the old one and just call onCreate
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        this.onCreate(db);
    }

    // Add a drug to the database
    public boolean addDrug(DB_DrugModel drugModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        return addDrugInternal(db, drugModel);
    }

    // Add a drug to the database
    // MUST BE CALLED AFTER OPENING A WRITABLE DATABASE OR IN onCreate()!
    private boolean addDrugInternal(SQLiteDatabase db, DB_DrugModel drugModel) {
        // Similar to a hash map
        ContentValues cv = new ContentValues();
        // We do not need to put in drug id since drugid auto increments
        cv.put(COL_DRUG_NAME, drugModel.getDrugName());
        cv.put(COL_DESC_ENGLISH, drugModel.getDescription_English());
        cv.put(COL_DESC_KAREN, drugModel.getDescription_Karen());

        long result = db.insert(TABLE_ID_TO_DRUG, null, cv);

        if (result == -1) {
            return false;
        }

        return result != -1;
    }

    // Add the base drugs to the db
    private boolean addAllDrugs(SQLiteDatabase db) {
        /// START TEMP
        // For now, do this, eventually maybe have some file interpreter?
        DB_DrugModel[] inputModels = {
                new DB_DrugModel("Dihydrogen Monoxide", "Cures dehydration", "1ကရင်"),
                new DB_DrugModel("Funny gas", null, "2ကရင်"),
                new DB_DrugModel("Microplastics", "Tasty!", "3ကရင်"),
                new DB_DrugModel("Placebex", "Modern marvel cure-all", "4ကရင်")

        };
        /// END TEMP

        boolean out = true;
        for (DB_DrugModel drugModel : inputModels) {
            boolean b = addDrugInternal(db, drugModel);
            out &= b;
            if(!b) {
                break;
            }
        }
        return out;
    }

    /* ========================================================================================== *
     * Getters
     * ========================================================================================== */

    public List<DB_DrugModel> getAllDrugs() {
        // Get data from the database
        String queryString = "SELECT * FROM " + TABLE_ID_TO_DRUG;
        return extractDrugModels(queryString);
    }



    // Get the drugModel associated with the id
    public List<DB_DrugModel> getDrugByID(int id) {
        if (id < 0) { return null; }

        //select * from table where information
        String idQuery = "SELECT * FROM " + TABLE_ID_TO_DRUG + " WHERE " +
                COL_DRUG_ID + " == " + id + ";";

        return extractDrugModels(idQuery);
    }

    // Return all drugs with names similar to input
    ///  TEMP matching *input* (* is wildcard)
    public List<DB_DrugModel> getDrugByName(String input) {
        if (input == null || input.length() == 0) { return null; }

        String nameQuery = "SELECT * FROM " + TABLE_ID_TO_DRUG + " WHERE " +
                COL_DRUG_NAME + " LIKE '%" + input + "%';";

        return extractDrugModels(nameQuery);
    }

    // This will run the sql statement, then parse the results into the return List
    private List<DB_DrugModel> extractDrugModels(String queryString) {
        Log.i("DB_DEMO", "Searching with query'" + queryString + "'");
        List<DB_DrugModel> returnList = new ArrayList<>();

        // Read so that we don't mutex lock it
        SQLiteDatabase db = this.getReadableDatabase();

        // Cursor is the result set of SQLite
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // Loop through result set (cursor) and create new customer objects. Put them into
            // return list. Do this while we can move to a new line
            do {
                // The columns have to be hard coded...
                int drugId = cursor.getInt(0);
                String drugName = cursor.getString(1);
                String description_English = cursor.getString(2);
                String description_Karen = cursor.getString(3);

                Log.w("DB_DEMO", "got name " + drugName);

                DB_DrugModel drugModel = new DB_DrugModel(drugId, drugName,
                        description_English, description_Karen);
                returnList.add(drugModel);
            } while(cursor.moveToNext());
        } // else. Failure do not add anything to the list


        // We are done with the database, allow others to use.
        cursor.close();
        db.close();
        return returnList;
    }

}
