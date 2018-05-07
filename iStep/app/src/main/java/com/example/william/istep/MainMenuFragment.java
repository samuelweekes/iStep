package com.example.william.istep;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.zip.Inflater;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.PopupWindowCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.william.istep.R.layout.main_menu_fragment;
import static com.example.william.istep.R.layout.popup_view;

/**
 * The mainMenuFragment class contains the code to control the step counter sensor and reset step button.
 */
public class MainMenuFragment extends Fragment {

private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notificationId;
    private RemoteViews remoteView;

private int goal;

    /**
     * Declare a TextView to display the counter's sensitivity, steps taken and welcome message.
     */
    private TextView textSensitive, textViewSteps, welcomeMessage, distanceWalked;


    /**
     * Declare a Button to access the settings page.
     */
    private Button settingsButton;

    /**
     * Declare a SensorManager.
     */
    private SensorManager sensorManager;

    /**
     * Declare a float variable for the acceleration, previousY value, and currentY value.
     */
    private float acceleration, previousY, currentY;

    /**
     * Declare an integer to represent the number of steps.
     */
    private int numSteps, threshold;

    /**
     * Instantiate the GraphManager.
     */
    GraphManager gm = new GraphManager();
    DatabaseOperations dbOps;



    /**
     * Default constructor.
     */
    public MainMenuFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbOps= new DatabaseOperations(getActivity());
        dbOps.Open();

        //build notification
        notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        remoteView = new RemoteViews(getContext().getPackageName(), R.layout.notification);
        remoteView.setImageViewResource(R.id.walkingimage,R.drawable.foot);
        remoteView.setTextViewText(R.id.textView,"You've hit your steps goal for today!");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the main menu layout.
        View myInflatedView = inflater.inflate(main_menu_fragment, container, false);


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        goal = Integer.parseInt(sp.getString("userGoal","10000"));

        /**
         * SharedPreference to check for first time start up. If it is the app's first start up, a Popup View will appear.
         */
        SharedPreferences prefs = getContext().getSharedPreferences("",Context.MODE_PRIVATE);

        boolean hasVisited = prefs.getBoolean("HAS_VISITED_BEFORE",false);
        if(!hasVisited){
           //Toast.makeText(getActivity(),"Welcome to iStep! You can start counting your steps but to fully utilise " +
             //    "the app we encourage you to enter your details in the settings page, accessed by clicking the icon in the corner",Toast.LENGTH_LONG).show();
            showPopup(myInflatedView);
            prefs.edit().putBoolean("HAS_VISITED_BEFORE",true).commit();
        }




        /**
         * Attach textViewSteps to the relevant .xml, namely ID count, to display the number of steps
         * taken to the user.
         */
        textViewSteps = (TextView) myInflatedView.findViewById(R.id.count);

        /**
         *
         */
        distanceWalked = (TextView) myInflatedView.findViewById(R.id.actual_distance);

        /**
        * Attach the settingsButton to the relevant .xml, namely settings_button, to display the settings button to the user.
        */
        settingsButton = (Button) myInflatedView.findViewById(R.id.settings_button);


        /**
         * attach the welcomeMessage TextView with the relevant .xml, namely the ID welcome_message.
         */
        welcomeMessage = (TextView) myInflatedView.findViewById(R.id.welcome_message);

        /**
         * Assign the threshold value.
         */
        threshold = 10;

        /**
         * Assign the Y,Z axis values and step numbers.
         */
        previousY = 0;
        currentY = 0;
        numSteps = dbOps.getSteps();


        /**
         * Assign the acceleration value.
         */
        acceleration = 0.00f;

        /**
         * Enable the Accelerometer listener.
         */
        enableAccelerometerListening();

        /**
        * Create the Event Listener and Event Handler for the settings button.
        */
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launch an intent to take the user to the Preferences Screen when clicked.
                Intent i = new Intent(getActivity(), MyPreferencesActivity.class);
                startActivity(i);
            }
        });

        /**
         * Display a welcome message to the user which takes the user's name from the preferences screen. This welcome message is also contained
         * within onResume (if the user changes their input name whilst using the app).
         */
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        welcomeMessage.setText("Hi "+ sp.getString("username","User") +", Track your steps!");
        goal = Integer.parseInt(sp.getString("userGoal","10000"));
        return myInflatedView;



    }

    /**
     * Method to enable and register the Accelerometer listener.
     */
    private void enableAccelerometerListening() {
        // Initialize the Sensor Manager
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }

    /**
     * onResume callback method contains a reference to the welcome message (incase the user changes their input name whilst using the app.
     */
    public void onResume(){
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        welcomeMessage.setText("Hi "+ sp.getString("username","No name") +", Track your steps!");
        dbOps.Open();
        goal = Integer.parseInt(sp.getString("userGoal","10000"));
    }


    // Event handler for accelerometer Events
    private SensorEventListener sensorEventListener =
            new SensorEventListener() {

                // Listens for change in Acceleration, Displays and Computes the Steps
                public void onSensorChanged(SensorEvent event) {

                    //Gather the values from accelerometer
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    // Fetch the current Y
                    currentY = y;

                    // Measure if a step is taken
                    try {
                        if (Math.abs(currentY - previousY) > threshold) {
                            numSteps++;
                            Thread.sleep(10);
                            textViewSteps.setText(String.valueOf(numSteps));

                            gm.setSteps(numSteps);
                            distanceWalked.setText((calculateDistance(numSteps)));
                            //invokes the SageSteps method, which will commit number of steps to storage at midnight.
                            SaveSteps();

                        }
                    } catch (Exception ex) {

                    }



                    // Store the previous Y
                    previousY = y;


                    //if goal reached, send notification
                    if ((int)numSteps ==goal){
                        Intent notification_intent = new Intent(getContext(),MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),0,notification_intent,0);
                        builder = new NotificationCompat.Builder(getContext());
                        builder.setSmallIcon(R.drawable.foot).setAutoCancel(true).setCustomContentView(remoteView)
                                .setContentIntent(pendingIntent);
                        notificationManager.notify(notificationId,builder.build());
                    }

                }

                /**
                 * Empty method required for the sensor.
                 */
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }

            };

    /**
     * Method invokes the insert data method after checking the time is appropriate.
     //hard coded to upload at midnight.
     */
    public void SaveSteps() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        if (dbOps.getSteps() == 0){
            dbOps.insertData(numSteps);}
        else{
            dbOps.updateData(numSteps);
        }

    }

    public void onPause(){
        super.onPause();
        if( dbOps != null) {
            dbOps.updateData(numSteps);
        } else {
            dbOps.insertData(numSteps);
            dbOps.Close();
            }


    }

    public void onDestroy(){

        if (dbOps.getSteps() == 0){
            dbOps.insertData(numSteps);}
        else{
            dbOps.updateData(numSteps);
            dbOps.Close();
            super.onDestroy();
        }

    }

    public String calculateDistance(int steps1){
        DecimalFormat df = new DecimalFormat("#.00");

        double distanceWalkedInches=steps1*28;
        double distanceWalkedMiles = distanceWalkedInches/63360;

        String distanceWalked = df.format(distanceWalkedMiles);
        return distanceWalked;

    }

    public void showPopup(View anchorView) {

        View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup_view,null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT);






        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

    }


}


