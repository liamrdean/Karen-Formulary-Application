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

    public static String[] dictonary = new String[0];

    /* Constants */
    // Change version only when you want the to overwrite previous versions.
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_ID_TO_DRUG = "TABLE_ID_TO_DRUG";
    public static final String COL_ID_STRING = "DRUG_ID";
    public static String COL_NAME_STRING = "DRUG_NAME";
    public static final String COL_DOSAGE_DISPLAY_STRING = "DOSAGE";
    public static final String COL_DESCRIPTION_DISPLAY_STRING = "DESCRIPTION";
    public static final String DRUG_CSV_FILE = "smallMedTestDatabase.csv";
    // String constants _{EN|KA}\\z
    // Regex to get either _EN or _KA at the end of a string
    public static final String KAREN_SUFFIX = "_KA";
    public static final String ENGLISH_SUFFIX = "_EN";
    public static final String LANGUAGE_SUFFIX_REGEX = "(_KA\\z)|(_EN\\z)";
    private SQLiteDatabase database;



    public static ActivityMain activityMain;

    /* Dynamic column names*/
    public static List<String> sqlColStrings;
    public static HashMap<String, Integer> headerToIndex;
    // Despite the name, no _EN or _KA should be in this, just the names before _EN or _KA
    public static String[] languageIndependentHeaders; // Internal column names
    // The display names of the headers (This is what is displayed to the user)
    public static HashMap<String, String> drugDisplayHeaders;

    private CSVReader csvReader;

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
        this.activityMain = (ActivityMain) context;

        InputStream inStream;


        // Load the CSV headers
        try {
            assert this.activityMain != null;
            Resources resources = this.activityMain.getResources();
            if (resources == null) {
                return;
            }

            AssetManager manager = resources.getAssets();
            if (manager == null) {
                return;
            }

            inStream = manager.open(DRUG_CSV_FILE);
            InputStreamReader inputCSVReader = new InputStreamReader(inStream);
            csvReader = new CSVReaderBuilder(inputCSVReader).build();
            loadCSVHeaders(csvReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (database != null) {
            Log.i("DB_HELPER", "calling on create");
            this.onCreate(database);
        } else {
            Log.i("DB_HELPER", "Not calling on create");
        }

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
        return addLanguageSuffix(s, ActivityMain.isKaren);
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
        String[] displayHeaders = csvReader.readNext();
        // Process the headers
        sqlColStrings = new ArrayList<>();
        drugDisplayHeaders = new HashMap<>();
        headerToIndex = new HashMap<>();
        // Ensure that the id column is included
        headerToIndex.put(COL_ID_STRING, 0);
        sqlColStrings.add(COL_ID_STRING);
        drugDisplayHeaders.put(COL_ID_STRING, "ERROR: USER SHOULD NOT BE ABLE TO SEE DRUG ID");


        // Process the name string
        //COL_NAME_STRING = drugHeaders[0].trim();
        headerToIndex.put(COL_NAME_STRING, 1);
        sqlColStrings.add(COL_NAME_STRING);
        drugDisplayHeaders.put(COL_NAME_STRING, "ERROR: Display of the drug name column should never appear");

        // This hash set will contain each name
        HashSet<String> headerSet = new HashSet<>();
        // Process the CSV headers into the sql columns & create the languageDependency list
        for (int i = 1; i < drugHeaders.length; i++) {
            if (!drugHeaders[i].isEmpty()) {
                String s = drugHeaders[i].trim().toUpperCase();
                sqlColStrings.add(s);
                headerToIndex.put(s, i + 1); // 1 is for the drug name column, so do i + 1
                String displayHeader = displayHeaders[i].trim();
                String DISPLAY_HEADER = displayHeader.toUpperCase();

                // When given a discription or a dosage column use the pre defined display values since they should not be seen
                if (DISPLAY_HEADER.startsWith(COL_DOSAGE_DISPLAY_STRING)) {
                    drugDisplayHeaders.put(s, COL_DOSAGE_DISPLAY_STRING);
                } else if (DISPLAY_HEADER.startsWith(COL_DESCRIPTION_DISPLAY_STRING)) {
                    drugDisplayHeaders.put(s, COL_DESCRIPTION_DISPLAY_STRING);
                } else {
                    drugDisplayHeaders.put(s, "    " + displayHeader);
                }

                /*
                // TEMP
                String[] a = s.split(LANGUAGE_SUFFIX_REGEX);
                if (a.length != 0) {
                    headerSet.add(a[0]);
                }
                */
                String noLanguage = this.removeLanguageSuffix(s);
                if (noLanguage != null && !noLanguage.isEmpty()) {
                    headerSet.add(noLanguage);
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
        List<String> list = new ArrayList<>();
        Log.i("InTeam", "Testing " + Arrays.toString(dictonary));

        dictonary = null;
        while ((values = csvReader.readNext()) != null) {
            Log.i("DEMOP adding", Arrays.toString(values));
            ContentValues cv = new ContentValues(sqlColStrings.size() - 1);

            list.add(values[0].trim());
            // Put all but the auto-assigned id in. That is why i starts at 1
            for (int i = 1; i < sqlColStrings.size(); i++) {
                String val = values[i - 1].trim();
                // Since % is a special character in SQL, replace it with the delimited version
                val = val.replace("%", "[%]");
                cv.put(sqlColStrings.get(i), val);
            }

            // Insert into the database
            if (-1 == db.insert(TABLE_ID_TO_DRUG, null, cv)) {
                Log.w("SQL_CSV_PARSER", "Failed to add drug " + values[0]);
            }
        }

        // Create the list of names
        dictonary = list.toArray(new String[0]);
        Log.i("InTeam", "Testing " + Arrays.toString(dictonary) + list.toString());
    }

    // This is called the first time a database is accessed.
    // There should only be code to create a new database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DB_HELPER", "onCreate has been called");
        this.database = db;
        InputStream inStream;
        /*
         * This does in order:
         *   Load the headers from the CSV file
         *   Create the table(s) in the database
         *   Read the database and load it into the table(s)
         */

        try {
            assert this.activityMain != null;
            Resources resources = this.activityMain.getResources();
            if (resources == null) {
                Log.w("DB_HELPER", "Resources is null");
                return;
            }

            AssetManager manager = resources.getAssets();
            if (manager == null) {

                Log.w("DB_HELPER", "AssetManager is null");
                return;
            }
            Log.i("DB_HELPER", "Good resources");

            inStream = manager.open(DRUG_CSV_FILE);
            InputStreamReader inputCSVReader = new InputStreamReader(inStream);
            csvReader = new CSVReaderBuilder(inputCSVReader).build();
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
        Log.i("DB_HELPER", "onUpgrade has been called");
        database = db; this.onCreate(db);
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

    private DB_DrugModel fillInDrugModel(Cursor cursor) {
        int i = 0;
        int drugId = cursor.getInt(i++);
        String drugName = cursor.getString(i++);

        HashMap<String, String> infoEN = new HashMap<>();
        HashMap<String, String> infoKA = new HashMap<>();

        for(; i < cursor.getColumnCount(); i++) {
            String s = cursor.getString(i);
            // Replace SQL delimited '%' ("[%]") with "%"
            s = s.replace("[%]", "%");

            Log.i("DEMOload",  i + "/" + cursor.getColumnCount() + "=" + s);
            Log.i("DEMOOO", Arrays.toString(sqlColStrings.toArray()));

            if (s != null && !s.isEmpty()) {
                String colWithLang = sqlColStrings.get(i);
                String col = removeLanguageSuffix(colWithLang);
                Log.i("DEMOload" + i, colWithLang + " " + Boolean.toString(colWithLang.endsWith(KAREN_SUFFIX)));

                // Insert based on language
                if (colWithLang.endsWith(KAREN_SUFFIX)) {
                    Log.i("DEMOload" + i, "KA add " + s);
                    infoKA.put(col, s);
                } else {
                    Log.i("DEMOload" + i, "EN add " + s);
                    infoEN.put(col, s);
                }
            }

        }
        DB_DrugModel drugModel = new DB_DrugModel(drugId, drugName, infoEN, infoKA);
        return drugModel;
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
                returnList.add(fillInDrugModel(cursor));
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

    // Return all drugs with names similar to input
    ///  TEMP matching *input* (* is wildcard)
    public List<DB_DrugModel> getDrugsByName(String input) {
        if (input == null || input.length() == 0) { return null; }

        String nameQuery = "SELECT * FROM " + TABLE_ID_TO_DRUG + " WHERE " +
                COL_NAME_STRING + " LIKE '%" + input + "%';";

        return extractDrugModels(nameQuery);
    }


}
