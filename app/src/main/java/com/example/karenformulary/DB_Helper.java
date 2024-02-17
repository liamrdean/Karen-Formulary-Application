package com.example.karenformulary;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DB_Helper extends SQLiteOpenHelper {

    public static final String TABLE_ID_TO_DRUG = "TABLE_ID_TO_DRUG";
    // This repersents each column name. This labels should match the one in the CSV file.
    // This also means stuff happens
    public enum ColNames {
        ID("DRUG_ID"),
        NAME("DRUG_NAME"),
        DESC_EN("DESCRIPTION_EN"),
        DESC_KA("DESCRIPTION_KA"),
        FOOD_EN("FOOD_EN"),
        FOOD_KA("FOOD_KA"),
        PREG_EN("PREGNANCY_EN"),
        PREG_KA("PREGNANCY_KA")
        ;

        String label;
        ColNames(String label) {
            this.label = label;
        }
    }

    private final MainActivity mainActivity;

    public DB_Helper(@Nullable Context context) {
        super(context, "drugDB", null, 1);
        this.mainActivity = (MainActivity) context;
    }

    // Temporary placement
    // Load the data base from a much easier to manage csv file.
    // MUST BE CALLED AFTER OPENING A WRITABLE DATABASE OR IN onCreate()!
    private boolean loadCSV(SQLiteDatabase db) {
        String csvFile = "test.csv";

        Log.i("DEMOCSV", "CSV");
        InputStream inStream = null;

        try {
            assert this.mainActivity != null;
            Resources resources = this.mainActivity.getResources();
            if (resources == null) {
                return false;
            }

            AssetManager manager = resources.getAssets();
            if (manager == null) {
                return false;
            }

            inStream = manager.open(csvFile);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));

            // Do this to shed the labels at the top of the CSV file
            String line = buffer.readLine();
            ColNames[] colNames = ColNames.values();

            // Ensure that the column names match with the CSV file
            // This is done to ensure that the data will match correctly
            if (line != null) {
                String[] csvColNames = line.split(",");
                Log.i("DEMOC", Arrays.toString(csvColNames));
                for(int i = 1; i < colNames.length; i++) {
                    String name = csvColNames[i-1].trim();
                    String colName = colNames[i].label;
                    if (!colName.equals(name)) {
                        Log.w("SQL CSV", "THE CSV COLUMN '" + name +
                                "' does not equal '" + colName + "'");
                        return false;
                    }
                }
            }

            // Load each line into the database
            while ((line = buffer.readLine()) != null) {
                String[] values = line.split(",");
                Log.i("DEMOC", Arrays.toString(values));
                if (values.length != colNames.length - 1) {
                    // Ensure that there are as many items as columns (minus ID)
                    Log.d("SQL_CSV", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues cv = new ContentValues(colNames.length - 1);
                /*
                 * Put all but the id in. Assumes that the order of columns in the csv matches the
                 * order in this file
                 */
                for (int i = 1; i < colNames.length; i++) {
                    String val = values[i-1].trim();
                    if (val.equals(DrugInfo.nullId)) {
                        // The cast is weird but it makes android studio be quite so...
                        cv.put(colNames[i].label, (String) null);
                    } else {
                        cv.put(colNames[i].label, values[i - 1].trim());
                    }
                }

                if (-1 == db.insert(TABLE_ID_TO_DRUG, null, cv)) {
                    Log.w("SQL_CSV_PARSER", "Failed to add " + values[0]);
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SQL_CSV", "Failed to load csv file " + e);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("SQL_CSV", "" + e);
        }

        return false;
    }


    // This is called the first time a database is accessed. There should only be code to create a new database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table (ID, DRUG_NAME)
        /* LEGACY
        String createTableStatement = "CREATE TABLE " + TABLE_ID_TO_DRUG + " (" + COL_DRUG_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_DRUG_NAME + " TEXT, "
                + COL_DESC_ENGLISH + " TEXT, " + COL_DESC_KAREN + " TEXT);";
        */

        // Procedurally create the table creation statement
        StringBuilder createTableStatement = new StringBuilder("CREATE TABLE " + TABLE_ID_TO_DRUG +
                " (" + ColNames.ID.label + " INTEGER PRIMARY KEY AUTOINCREMENT");
        // Add the rest of the columns
        ColNames[] colNames = ColNames.values();
        for (int i = 1; i < colNames.length; i++) {
            createTableStatement.append(", ").append(colNames[i].label).append(" TEXT");
        }
        createTableStatement.append(");");

        db.execSQL(createTableStatement.toString());

        // Initialize the stuff into the DB
        loadCSV(db);
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
                ColNames.ID.label + " == " + id + ";";

        return extractDrugModels(idQuery);
    }

    // Return all drugs with names similar to input
    ///  TEMP matching *input* (* is wildcard)
    public List<DB_DrugModel> getDrugByName(String input) {
        if (input == null || input.length() == 0) { return null; }

        String nameQuery = "SELECT * FROM " + TABLE_ID_TO_DRUG + " WHERE " +
                ColNames.NAME.label + " LIKE '%" + input + "%';";

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
                DrugInfo infoEN = new DrugInfo(cursor.getString(2));
                DrugInfo infoKA = new DrugInfo(cursor.getString(3));

                Log.w("DB_DEMO", "got name " + drugName);

                DB_DrugModel drugModel = new DB_DrugModel(drugId, drugName, infoEN, infoKA);
                returnList.add(drugModel);
            } while(cursor.moveToNext());
        } // else. Failure do not add anything to the list


        // We are done with the database, allow others to use.
        cursor.close();
        db.close();
        return returnList;
    }

}
