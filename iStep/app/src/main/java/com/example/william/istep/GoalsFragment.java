package com.example.william.istep;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;

import static com.example.william.istep.R.layout.abc_alert_dialog_button_bar_material;
import static com.example.william.istep.R.layout.goals_fragment;
import static com.example.william.istep.R.layout.main_menu_fragment;

/**
 * This fragment displays a graph which shows progress towards step and calorie goals.
 */
public class GoalsFragment extends Fragment {

    /**
     * Declare a button which when clicked will change the graph to the calorie goal.
     */
    Button calories_graph;
    Button steps_button;

    /**
     * Declare a view.
     */
    View myView;

    /**
     * Instantiate the GraphManager.
     */
    GraphManager gm = new GraphManager();
    int steps = gm.getSteps();

    /**
     * Default constructor.
     */
    public GoalsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.goals_fragment, container, false);

        //recover userWeight from shared preferences for working out calories burned
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final int userWeight = Integer.parseInt(sp.getString("userweight","0"));
        //recover goal from shared preferences to create pieChart with goal
        final int goal = Integer.parseInt(sp.getString("userGoal","10000"));

        //make pie chart on creation of view (see GraphManager class for details)
        gm.makePieChart(myView,steps, goal, "Today's step goal", "Steps taken", "Steps remaining", getContext());

        //button to allow user to switch between calories burned and steps taken to be displayed as a piechart
        calories_graph = (Button) myView.findViewById(R.id.calories_button);




        calories_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recover gender from shared preferences to display calorie expenditure in calories goal pieChart
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String gender = sp.getString("gender","F");
                int caloriesGoal;
                //if male calorie expenditure =2500, if female = 2000, if not entered correctly, set to 2250 (average of two values)
                if (gender.equalsIgnoreCase("m")){
                caloriesGoal= 2500;
            } else if (gender.equalsIgnoreCase("f")) {
                caloriesGoal=2000;
            } else caloriesGoal =2250;
                int caloriesB = gm.convertStepsToCalories(gm.getSteps(),userWeight);
                //makescalories pieCHart (See GraphManager class for details)
                gm.makePieChart(myView, caloriesB , caloriesGoal, "Calories burned from steps", "Calories burned", "Total calories", getContext());
            }
        });
        // button to switch back to steps goal progress pieChart.
        steps_button = (Button) myView.findViewById(R.id.steps_button_goals);
        steps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                final int goal = Integer.parseInt(sp.getString("userGoal","10000"));
                gm.makePieChart(myView, gm.getSteps(), goal, "Today's step goal", "Steps taken", "Steps remaining", getContext());
            }
        });
        return myView;
    }

    /**
     * each time the tab is made visable the graph is updated with current steps taken.
     * @param visible
     */
    @Override
    public void setMenuVisibility(final boolean visible){

        super.setMenuVisibility(visible);


        if(visible){
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int steps = gm.getSteps();
            final int goal = Integer.parseInt(sp.getString("userGoal","10000"));
            gm.makePieChart(myView,steps, goal, "Today's step goal", "Steps taken", "Steps remaining", getContext());
        }
    }

}
