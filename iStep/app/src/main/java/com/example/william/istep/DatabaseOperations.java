package com.example.william.istep;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class contains the methods for functional operations of the database.
 * These include adding, updating and retrieving information from the database.
 * Created by Daniel.
 */

public class DatabaseOperations {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DatabaseOperations(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Opens connection to the database.
     *
     * @throws SQLException
     */

    public void Open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    /**
     * Closes connection to the database.
     */
    public void Close() {
        dbHelper.close();
    }


    /**
     * Returns current date as an int.
     */
    public int DateFinder() {
        Calendar Cal = Calendar.getInstance();
        int date = Cal.get(Calendar.DATE);

        return date;
    }

    /**
     * method for inserting data.  Date is set by the current time.
     * method returns true if data upload is successful.
     */

    public boolean insertData(int steps) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DATE, DateFinder());
        contentValues.put(DatabaseHelper.STEPS, steps);
        long result = db.insert(DatabaseHelper.DATABASE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method for retrieving data from the database.
     *
     * @return
     */
    public int getSteps() {
        long Rows = DatabaseUtils.queryNumEntries(db, DatabaseHelper.DATABASE_NAME);
        int numRow = (int) Rows;
        int stepR;


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor steps = db.rawQuery(" SELECT " + DatabaseHelper.STEPS + " FROM " + DatabaseHelper.DATABASE_NAME +
                " WHERE " + DatabaseHelper.ID + " = " + numRow + " AND " + DatabaseHelper.DATE + " = " + DateFinder(), null);
        if (steps.getCount() > 0) {

            steps.moveToFirst();
            stepR = steps.getInt(0);
        } else {
            stepR = 0;
        }
        steps.close();

        return stepR;
    }

    /**
     * Method for updating an existing entry in the database.
     *
     * @param steps
     * @return
     */
    public boolean updateData(int steps) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ID, DatabaseHelper.ID);
        contentValues.put(DatabaseHelper.DATE, DateFinder());
        contentValues.put(DatabaseHelper.STEPS, steps);
        db.update(DatabaseHelper.DATABASE_NAME, contentValues, "ID =?", new String[]{DatabaseHelper.DATE});

        return true;


    }

    public int getTotalSteps() {
        int totalSteps = 0;
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DatabaseHelper.DATABASE_NAME);

        int[] countingSteps = new int[numRows];
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.STEPS + " FROM " + DatabaseHelper.DATABASE_NAME, null);
        try{
            for(int loop=0;loop<numRows;loop++){
                cursor.moveToFirst();
                countingSteps[loop] = cursor.getInt(0);
            }}
        finally {
            cursor.close();
            db.close();
            }
        for (int counter = 0 ; counter<countingSteps.length;counter++){
            totalSteps +=countingSteps[counter];
        }

        return totalSteps;
    }

    public int caloriesBurned() {
        int caloriesBurned = 0;
        return caloriesBurned;
    }

    public ArrayList<DatabaseEntry> PopulateGraph() {

        /**
         * This method will populate an ArrayList from the Database which can be used to populate the Statistics graph.
         */
        ArrayList<DatabaseEntry> StepsTaken = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int day = 0;
        /**
         * The Rows function counts the number of entries in the db and returns it as a long.
         * numRow converts this value to an int.  This value will be equal to the ID of the most recent entry.
         */
        int numRow = (int)DatabaseUtils.queryNumEntries(db, DatabaseHelper.DATABASE_NAME);

        int stepR1 = 0;
        int stepR2 = 0;
        int stepR3 = 0;
        int stepR4 = 0;
        int stepR5 = 0;
        int stepR6 = 0;
        int stepR7 = 0;

        /**
         * SearchID finds the auto incremented ID by taking the value of the most recent entry, and counting back the number of entries.
         */


        /**
         * This  loop will continue until 7 entries have been created.
         * It searches for the most recent entry, returning the steps and ID
         * Each time it increments through, it looks one day back.
         * This will create an arraylist capable of populating the graph for the last seven days.
         */

            int searchID = (numRow - day);
            Cursor steps = db.rawQuery("SELECT " + DatabaseHelper.STEPS + " FROM " + DatabaseHelper.DATABASE_NAME + " WHERE " + DatabaseHelper.ID + " = " + (numRow), null);
            Cursor steps2 = db.rawQuery("SELECT " + DatabaseHelper.STEPS + " FROM " + DatabaseHelper.DATABASE_NAME + " WHERE " + DatabaseHelper.ID + " = " + (numRow -1), null);
           Cursor steps3 = db.rawQuery("SELECT " + DatabaseHelper.STEPS + " FROM " + DatabaseHelper.DATABASE_NAME + " WHERE " + DatabaseHelper.ID + " = " + (numRow -2), null);
           Cursor steps4 = db.rawQuery("SELECT " + DatabaseHelper.STEPS + " FROM " + DatabaseHelper.DATABASE_NAME + " WHERE " + DatabaseHelper.ID + " = " + (numRow -3), null);
            Cursor steps5 = db.rawQuery("SELECT " + DatabaseHelper.STEPS + " FROM " + DatabaseHelper.DATABASE_NAME + " WHERE " + DatabaseHelper.ID + " = " + (numRow -4), null);
            Cursor steps6 = db.rawQuery("SELECT " + DatabaseHelper.STEPS + " FROM " + DatabaseHelper.DATABASE_NAME + " WHERE " + DatabaseHelper.ID + " = " + (numRow -5), null);
           Cursor steps7 = db.rawQuery("SELECT " + DatabaseHelper.STEPS + " FROM " + DatabaseHelper.DATABASE_NAME + " WHERE " + DatabaseHelper.ID + " = " + (numRow -6), null);
            /**
             * These method calls retrieve the information from the cursor.
             */

                steps.moveToFirst();
                stepR1 = steps.getInt(0);
                steps2.moveToFirst();
                stepR2 = steps.getInt(0);
               steps3.moveToFirst();
                stepR3 = steps.getInt(0);
                steps4.moveToFirst();
                stepR4 = steps.getInt(0);
               steps5.moveToFirst();
                stepR5 = steps.getInt(0);
               steps6.moveToFirst();
                stepR6 = steps.getInt(0);
               steps7.moveToFirst();
                stepR7 = steps.getInt(0);




            /**
             * Creates a database entry object which will be stored in the ArrayList
             */

            DatabaseEntry dbe = new DatabaseEntry(searchID, stepR1);
            DatabaseEntry dbe2 = new DatabaseEntry(searchID -1, stepR2);
            DatabaseEntry dbe3 = new DatabaseEntry(searchID -2, stepR3);
            DatabaseEntry dbe4 = new DatabaseEntry(searchID -3, stepR4);
            DatabaseEntry dbe5 = new DatabaseEntry(searchID -4, stepR5);
            DatabaseEntry dbe6 = new DatabaseEntry(searchID - 5, stepR6);
            DatabaseEntry dbe7 = new DatabaseEntry(searchID -6, stepR7);

            StepsTaken.add(dbe);
            StepsTaken.add(dbe2);
            StepsTaken.add(dbe3);
            StepsTaken.add(dbe4);
            StepsTaken.add(dbe5);
            StepsTaken.add(dbe6);
            StepsTaken.add(dbe7);


            day++;
            steps.close();
        db.close();


        return StepsTaken;


    }
}
