package com.example.william.istep;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;


public class StatisticsFragment extends Fragment {

    DatabaseHelper databaseHelper;

    DatabaseOperations dO;

    DatabaseEntry dE;

    SQLiteDatabase db;

    SharedPreferences sp;
    /**
     *  Instance variable for steps which should be retrieved from database
     */

    private int stepsCount;

    /*
     *  Instance variable for averageSteps
     */

    private int averageStepsCount;



    /*
     * Instance variable for kcal/weight value
     */

    private double kcalweight;

    /*
     *  Instance variable for calories burned
     */

    private double kcalBurned;

    /**
     * TextView to show calories burned
     */

    private TextView caloriesBurned;

    /**
     *  TextView to show average steps
     */

    private TextView averageSteps;

    /**
     *  TextView to show total steps
     */

    private TextView totalSteps;

    private int steps1;
    private int steps2;
    private int steps3;
    private int steps4;
    private int steps5;
    private int steps6;
    ArrayList<DatabaseEntry> dataBaseSteps;

    private ArrayList<Integer> stepsList = new ArrayList<>();

    /*
     * Required empty constructor for fragment
     */

    public StatisticsFragment() {
    }
    View inflatedView;
    GraphManager gm = new GraphManager();
    /*
     * OnCreates for the fragment
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // instantiate DatabaseOperations
        databaseHelper = new DatabaseHelper(getContext());
        dO= new DatabaseOperations(getContext());
        dE=new DatabaseEntry();
        db = databaseHelper.getWritableDatabase();
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //---------------------------------------------------------------------
        // need a method that returns an array list of integer values

        //----------------------------------------------------------------------
        //loop to populate steps vars with database values




        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.statistics_fragment, container, false);

        //Inflate text views
        caloriesBurned = (TextView)inflatedView.findViewById(R.id.calories_count);
        averageSteps = (TextView)inflatedView.findViewById(R.id.average_steps_count);
        totalSteps = (TextView)inflatedView.findViewById(R.id.total_steps_count);

        // random test values

        averageStepsCount = getAverageDailySteps();
        stepsCount = dO.getTotalSteps();
        kcalBurned = 100;

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int numRow = (int) DatabaseUtils.queryNumEntries(db, DatabaseHelper.DATABASE_NAME);

        //Assign values to text views
        averageSteps.setText(String.valueOf(getAverageDailySteps()));
        totalSteps.setText(String.valueOf(stepsCount));
        caloriesBurned.setText(String.valueOf(getTotalCaloriesBurned(dO.getTotalSteps())));

        dataBaseSteps = dO.PopulateGraph();
        //creates barChart (see GraphManager for details)
        gm.makeBarChart(inflatedView, "Steps for the last 7 days", dataBaseSteps, gm.getSteps());
        return inflatedView;

    }


    /**
     * Method returns the average daily steps for display in the Statistics tab.
     */
    public int getAverageDailySteps(){



        int totalDays = (int) DatabaseUtils.queryNumEntries(db, DatabaseHelper.DATABASE_NAME);
        int averageDailySteps = stepsCount/totalDays;


        return averageDailySteps;

    }




    public int getTotalCaloriesBurned(int totalSteps){


        int userWeight = Integer.parseInt(sp.getString("userweight","0"));
        int caloriesB = gm.convertStepsToCalories((float)dO.getTotalSteps(),(double)userWeight);

        return caloriesB;
    }






    //Updates barCHart each time the statstics tab is visable to ensure steps taken on the current day is up to date.
    @Override
    public void setMenuVisibility(final boolean visible){
        super.setMenuVisibility(visible);
        if(visible){
            gm.makeBarChart(inflatedView,"Steps taken",dataBaseSteps, gm.getSteps());
            averageSteps.setText(String.valueOf(getAverageDailySteps()));
            totalSteps.setText(String.valueOf(dO.getTotalSteps() +gm.getSteps()));
            caloriesBurned.setText(String.valueOf(getTotalCaloriesBurned(dO.getTotalSteps())));
        }
    }
}

