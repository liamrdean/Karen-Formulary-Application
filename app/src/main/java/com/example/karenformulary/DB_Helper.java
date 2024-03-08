package com.example.karenformulary;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class DB_Helper extends SQLiteOpenHelper {
    /* Constants */
    // Change version only when you want the to overwrite previous versions.
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_ID_TO_DRUG = "TABLE_ID_TO_DRUG";
    public static final String COL_ID_STRING = "DRUG_ID";
    public static String COL_NAME_STRING;
    public static final String DRUG_CSV_FILE = "small.csv";
    // String constants _{EN|KA}\\z
    // Regex to get either _EN or _KA at the end of a string
    // .*(_KA)|(_EN)\z
    public static final String KAREN_SUFFIX = "KA";
    public static final String ENGLISH_SUFFIX = "EN";
    public static final String LANGUAGE_SUFFIX_REGEX = "(_KA\\z)|(_EN\\z)";

    private final MainActivity mainActivity;

    /* Dynamic column names*/
    public static List<String> sqlColStrings;
    public static HashMap<String, Integer> headerToIndex;
    // Despite the name, no _EN or _KA should be in this, just the names before _EN or _KA
    public static String[] languageIndependentHeaders;

    /*
     * Headers: DRUG_ID, [ NAME, ...{*_EN *_KA} ]
     * Constant headers: Drug id, name
     * Language dependent: others
     *      get(STRING);
     *          - Based off language return the data
     *          - Return a direct Map?
     */

    // ASSUMES THAT THIS IS CALLED FROM THE MAIN ACTIVITY
    public DB_Helper(@Nullable Context context) {
        super(context, "drugDB", null, DB_Helper.DATABASE_VERSION);
        this.mainActivity = (MainActivity) context;
    }

    // Remove the language suffixes from a string
    public String removeLanguageSuffix(String s) {
        String[] array = s.split(LANGUAGE_SUFFIX_REGEX);
        if (array.length == 0) {
            return s;
        } else {
            return array[0];
        }
    }

    // Return s + CURRENT_LANGUAGE_SUFFIX
    public static String addCurrentLanguageSuffix(String s) {
        return addLanguageSuffix(s, MainActivity.isKaren);
    }

    public static String addLanguageSuffix(String s, boolean inKaren) {
        StringBuilder res = new StringBuilder(s);
        if (inKaren) {
            res.append(KAREN_SUFFIX);
        } else {
            res.append(ENGLISH_SUFFIX);
        }
        return res.toString();
    }

    // Load the headers from a CSV file
    private void loadCSVHeaders(@NonNull CSVReader csvReader) throws IOException {
        String[] drugHeaders = csvReader.readNext();
        // Process the headers
        sqlColStrings = new ArrayList<>();
        headerToIndex = new HashMap<>();
        // Ensure that the id column is included
        sqlColStrings.add(COL_ID_STRING);
        headerToIndex.put(COL_ID_STRING, 0);

        // Process the name string
        //COL_NAME_STRING = drugHeaders[0].trim();
        headerToIndex.put(COL_NAME_STRING, 1);

        // This hash set will contain each name
        HashSet<String> headerSet = new HashSet<>();
        // Process the CSV headers into the sql columns & create the languageDependency list
        for (int i = 1; i < drugHeaders.length; i++) {
            if (!drugHeaders[i].isEmpty()) {
                String s = drugHeaders[i].trim();
                sqlColStrings.add(s);
                headerToIndex.put(s, i + 1); // 1 is for the drug name column, so do i + 1
                /*
                // TEMP
                String[] a = s.split(LANGUAGE_SUFFIX_REGEX);
                if (a.length != 0) {
                    headerSet.add(a[0]);
                }
                */
                String noLanguage = removeLanguageSuffix(s);
                if (noLanguage != null && !noLanguage.isEmpty()) {
                    headerSet.add(s);
                }
            }
        }

        languageIndependentHeaders = headerSet.toArray(new String[0]);
    }


    // Load the data base from a much easier to manage csv file.
    // MUST BE CALLED AFTER OPENING A WRITABLE DATABASE OR IN onCreate()!
    private void loadCSV(SQLiteDatabase db, @NonNull CSVReader csvReader) throws IOException {
        //Map<String, String> values = new CSVReaderHeaderAware(inputCSVReader).readMap();
        /*
        CSVReaderHeaderAware headerAware = new CSVReaderHeaderAware(inputCSVReader);
        Log.i("SDEMOP:", headerAware.readMap().get("MEDICATION"));
        Log.i("SDEMOP:", headerAware.readMap().get("MEDICATION"));
        Log.i("SDEMOP:", headerAware.readMap().get("MEDICATION"));
        */

        Log.i("DEMOP", "Starting parse");

        // Load each line into the database
        String[] values;
        while ((values = csvReader.readNext()) != null) {
            Log.i("DEMOP adding", Arrays.toString(values));
            ContentValues cv = new ContentValues(sqlColStrings.size() - 1);

            // Put all but the auto-assigned id in. That is why i starts at 1
            for (int i = 1; i < sqlColStrings.size() - 1; i++) {
                String val = values[i - 1].trim();
                cv.put(sqlColStrings.get(i), val);
            }

            // Insert into the database
            if (-1 == db.insert(TABLE_ID_TO_DRUG, null, cv)) {
                Log.w("SQL_CSV_PARSER", "Failed to add drug " + values[0]);
            }
        }
    }

    // This is called the first time a database is accessed.
    // There should only be code to create a new database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        InputStream inStream;
        /*
         * This does in order:
         *   Load the headers from the CSV file
         *   Create the table(s) in the database
         *   Read the database and load it into the table(s)
         */

        try {
            assert this.mainActivity != null;
            Resources resources = this.mainActivity.getResources();
            if (resources == null) {
                return;
            }

            AssetManager manager = resources.getAssets();
            if (manager == null) {
                return;
            }

            inStream = manager.open(DRUG_CSV_FILE);
            InputStreamReader inputCSVReader = new InputStreamReader(inStream);
            CSVReader csvReader = new CSVReaderBuilder(inputCSVReader).build();
            loadCSVHeaders(csvReader);

            /*
             * Generate the SQL table from the CSV headers
             */
            StringBuilder tableBuilder = new StringBuilder("CREATE TABLE " + TABLE_ID_TO_DRUG +
                    " (" + COL_ID_STRING + " INTEGER PRIMARY KEY AUTOINCREMENT");
            // Add the rest of the columns
            for (int i = 1; i < sqlColStrings.size(); i++) {
                tableBuilder.append(", ").append(sqlColStrings.get(i)).append(" TEXT");
            }
            tableBuilder.append(");");
            Log.i("DEMOP SQL", tableBuilder.toString());
            db.execSQL(tableBuilder.toString());

            // Initialize the stuff into the DB
            loadCSV(db, csvReader);

        } catch (FileNotFoundException e) {
            Log.w("DB onCreate:", "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.w("DB onCreate:", "IOException");
            e.printStackTrace();
        }
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
                COL_ID_STRING + " == " + id + ";";

        return extractDrugModels(idQuery);
    }

    // Return all drugs with names similar to input
    ///  TEMP matching *input* (* is wildcard)
    public List<DB_DrugModel> getDrugByName(String input) {
        if (input == null || input.length() == 0) { return null; }

        String nameQuery = "SELECT * FROM " + TABLE_ID_TO_DRUG + " WHERE " +
                COL_NAME_STRING + " LIKE '%" + input + "%';";

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
                //DrugInfo infoEN = new DrugInfo(cursor.getString(2));
                //DrugInfo infoKA = new DrugInfo(cursor.getString(3));

                DrugInfo infoEN = new DrugInfo(cursor, false);
                DrugInfo infoKA = new DrugInfo(cursor, true);


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

    // Return the index of a header given it's string value
    public static int getHeaderIndex(String header){
        Integer i = headerToIndex.get(header);
        if (i == null) {
            return -1;
        }
        return i;
    }

}
