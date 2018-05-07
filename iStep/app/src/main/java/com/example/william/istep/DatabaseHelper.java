package com.example.william.istep;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    //These variables are the names of the database, and the columns within it.
    public static final String ID = "ID";
    public static final String DATE = "Date";
    public static final String STEPS = "Steps";
    public static final String DATABASE_NAME = "StepsDB";
    private static final int DATABASE_VERSION = 5;
    private static final String populateDatabase = "INSERT INTO " + DATABASE_NAME + " VALUES(NULL, 0 , 10000)";



//Defines tables.  Sets up database name and values using constants already created.  Primary Key established.
private static final String DATABASE_CREATE =
        "CREATE TABLE " + DATABASE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " INTEGER, " + STEPS + " INTEGER)";

    //constructor

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        for(int loop = 0; loop<6;loop++){
            db.execSQL(populateDatabase);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        this.onCreate(db);
    }

}