package com.example.abc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.abc.fragment.MapFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity {

    AppCompatRadioButton rdWeekly, rdMonthly, rdYearly;
    PieChart pieChart;
    BarChart barChart;
    ArrayList<BarEntry> barEntriesList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ntt_statistic_manager);

        PieChart pieChart = findViewById(R.id.chart);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        float occupiedValue = 50f; // Giá trị đại diện cho "Occupied"
        float availableValue = 30f; // Giá trị đại diện cho "Available"
        float maintenanceValue = 20f; // Giá trị đại diện cho "Under Maintenance"

        PieEntry occupiedEntry = new PieEntry(occupiedValue);
        PieEntry availableEntry = new PieEntry(availableValue);
        PieEntry maintenanceEntry = new PieEntry(maintenanceValue);

        pieEntries.add(occupiedEntry);
        pieEntries.add(availableEntry);
        pieEntries.add(maintenanceEntry);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");

        int[] colors = {Color.parseColor("#FF4081"), Color.parseColor("#00C853"), Color.parseColor("#9E9E9E")};
        pieDataSet.setColors(colors);
        pieDataSet.setDrawValues(false);
        pieChart.setHoleRadius(50f);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.animateXY(5000, 5000);
        pieChart.getDescription().setEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

//     BarChart

        barChart = findViewById(R.id.bar_chart);
        barEntriesList = new ArrayList<>();
        setValue();
        setUpChart();


    }

    private void setUpChart() {
        BarDataSet barDataSet = new BarDataSet(barEntriesList,"");
        barDataSet.setColor(Color.parseColor("#799BF9"));
        barDataSet.setDrawValues(false);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.2f);
        barChart.setData(barData);
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawAxisLine(true);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawAxisLine(true);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.invalidate();
    }

    private void setValue(){
        barEntriesList.add(new BarEntry(1,9));
        barEntriesList.add(new BarEntry(3,8));
        barEntriesList.add(new BarEntry(5,7));
        barEntriesList.add(new BarEntry(2,6));
        barEntriesList.add(new BarEntry(4,5));
        barEntriesList.add(new BarEntry(9,4));
        barEntriesList.add(new BarEntry(6,3));
        barEntriesList.add(new BarEntry(8,1));
        barEntriesList.add(new BarEntry(7,2));

    }

}