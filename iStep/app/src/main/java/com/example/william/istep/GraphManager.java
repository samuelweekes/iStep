package com.example.william.istep;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.example.william.istep.R.id.chart;

/**
 * Created by User on 11/04/2017.
 */

public class GraphManager {



//int for steps
    static int steps =0;
//sets the steps (called in main menu fragment each time a new step is taken to keep up to date)
    public void setSteps(int steps){
        this.steps = steps;

    }
//getter for steps, used in creation of graph to keep up to date.
    public int getSteps(){
        return (int)steps;
    }


    /**
     * make barChart method, takes the view of the fragment where it's displayed, a string for a title and
     * the 7 step values from the previous 7 days steps (from the database)
     * @param view
     * @param title
     * @param stepsList
     * @param steps7
     */
    public void makeBarChart(View view, String title, ArrayList<DatabaseEntry> stepsList, int steps7){
        //declare barchart
        ArrayList<Integer> listOfSteps = new ArrayList<>();




        for (int loop = 0; loop< stepsList.size();loop++){
            int steps = stepsList.get(loop).getSteps();

            listOfSteps.add(loop,steps);

        }



        BarChart barChart;
        //link to xml
        barChart = (BarChart) view.findViewById(R.id.statistics_chart);
        //create list to store data for the barChart
        List<BarEntry> entries1 = new ArrayList<>();


        entries1.add(new BarEntry(0f, (float)listOfSteps.get(0)));
        entries1.add(new BarEntry(1f, (float)listOfSteps.get(1)));
        entries1.add(new BarEntry(2f, (float)listOfSteps.get(2)));
        entries1.add(new BarEntry(3f, (float)listOfSteps.get(3)));
        entries1.add(new BarEntry(4f, (float)listOfSteps.get(4)));
        entries1.add(new BarEntry(5f, (float)listOfSteps.get(5)));
        entries1.add(new BarEntry(6f,(float)steps7));

        //add barchart data array to BarDataset Object
        BarDataSet set = new BarDataSet(entries1, title);
        BarData data = new BarData(set);
        //sets width of bars as displayed in graph
        data.setBarWidth(0.9f); // set custom bar width
        //gets rid of background lines
        barChart.setDrawGridBackground(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);

        //sets origin to 0
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        //set legend size
        Legend l = barChart.getLegend();
        l.setFormSize(16);
        l.setTextSize(16);

        //animate
        barChart.animateY(500);

        //put data in barChart object
        barChart.setData(data);
        //hide description
        barChart.getDescription().setEnabled(false);
        //update chart
        barChart.invalidate();





    }

    /**
     * Method to make PieChart. Takes view and context of the fragment in which it'll be showing as an arguement.
     * Steps will be taken user getSteps() to keep steps up to date.
     * Goal will be taken from shared preferences to get accurate goal from user.
     * Title will sppear in the middle of the "pie"
     * Segment1 and segment2 are the titles of each segment
     * @param view
     * @param steps1
     * @param goal
     * @param title
     * @param segment1
     * @param segment2
     * @param context
     */
    public void makePieChart(View view, int steps1, int goal, String title, String segment1, String segment2, Context context){


        //get remaining steps by subtracting user set goal from the number of steps taken.
        int remaining = goal-steps1;
        //create pie chart
        PieChart pieChart = (PieChart) view.findViewById(chart);
        //define data points
        PieEntry p1;
        PieEntry p2;

        //if else statment to display one graph while the user is working towards their goal, and a second graph if the goal has been reached.
        //if goal is reached for the day the graph will show how many steps they have taken for the day and a toast to tell them they have reached their goal.
        if (remaining<=0){

           p1 = new PieEntry(steps1, segment1);
            p2 = new PieEntry((0), segment2); //replace 10000 with goa
            CharSequence text = "You have reached your goal for today!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } else {
            p1 = new PieEntry((int)steps1, segment1);

             p2 = new PieEntry(((int)remaining), segment2);
        }

        //adding data to the pieChart
        List<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(p1);
        entries.add(p2);
        PieDataSet dataSet = new PieDataSet(entries,"");
        PieData data = new PieData(dataSet);

        //setting the colour, various other colourschemes commented out.
        ArrayList<Integer> colors = new ArrayList<Integer>();

        //for (int c : ColorTemplate.VORDIPLOM_COLORS)
        //colors.add(c);

        //for (int c : ColorTemplate.COLORFUL_COLORS)
        //    colors.add(c);

        for (int c : ColorTemplate.MATERIAL_COLORS)
           colors.add(c);

        // for (int c : ColorTemplate.LIBERTY_COLORS)
        //  colors.add(c);

        // for (int c : ColorTemplate.PASTEL_COLORS)
        //   colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        //formatting
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setHighlightEnabled(true);
        dataSet.setValueTextSize(30);
        //formatting size of legend
        Legend l = pieChart.getLegend();
        l.setTextSize(24);
        l.setFormSize(24);
        //formatting text label size
        pieChart.setEntryLabelTextSize(20);
        //animate pieChart on creation
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
        //diasable description
        pieChart.getDescription().setEnabled(false);
        //sets title parameter from methodcall to be displayed in the centre of the pie
        pieChart.setCenterText(title);
        pieChart.setCenterTextSize(24);
        //run data into pieChart object

        pieChart.setData(data);
        //refresh chart
        pieChart.invalidate();


    }


    /**
     * Method to determine the number of calories burned from the user weight and the number of steps taken.
     * @param stepsTaken
     * @param weight
     * @return
     */
    public int convertStepsToCalories (float stepsTaken, double weight){


        int steps = this.steps;
        int kcalweight = 0;



        if (weight < 48) {
            kcalweight = 25;
        } else if (weight < 52) {
            kcalweight = 27;
        } else if (weight < 58) {
            kcalweight = 30;
        } else if (weight < 63) {
            kcalweight = 32;
        } else if (weight < 68) {
            kcalweight = 34;
        } else if (weight < 71.5) {
            kcalweight = 37;
        } else if (weight < 75) {
            kcalweight = 39;
        } else if (weight < 79.5) {
            kcalweight = 42;
        } else if (weight < 84) {
            kcalweight = 44;
        } else if (weight < 88) {
            kcalweight = 47;
        } else if (weight < 93) {
            kcalweight = 49;
        } else if (weight < 97) {
            kcalweight = 52;
        } else if (weight < 103) {
            kcalweight = 54;
        } else if (weight < 109) {
            kcalweight = 58;
        } else if (weight < 117) {
            kcalweight = 62;
        } else if (weight < 122.5) {
            kcalweight = 65;
        } else if (weight < 129) {
            kcalweight = 68;
        } else {
            kcalweight = 75;
        }

        int kcalBurned = kcalweight * steps / 1000;
        return kcalBurned;
    }
}
