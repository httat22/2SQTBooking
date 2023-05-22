package com.example.abc.fragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import com.example.abc.R;
import com.example.abc.models.BarChartItem;
import com.example.abc.models.InfoRoomBookedModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private TextView tvMaintenanceValue, tvAvailableValue, tvOccupiedValue, tvWeekCurrent,
            tvTotalCheckOut, tvTotalCheckIn, tvWMY, tvNewBooking;
    private AppCompatRadioButton rdWeekly, rdMonthly, rdYearly;
    private PieChart pieChart;
    private BarChart barChart;
    private ArrayList<BarEntry> barEntriesList;
    private DatabaseReference timeRoomBookedRef = FirebaseDatabase.getInstance().getReference("time_room_booked");
    private DatabaseReference infoRoomBookedRef = FirebaseDatabase.getInstance().getReference("statistics_room");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);
        tvMaintenanceValue = view.findViewById(R.id.tvMaintenanceValue);
        tvAvailableValue = view.findViewById(R.id.tvAvailableValue);
        tvOccupiedValue = view.findViewById(R.id.tvOccupiedValue);
        tvWeekCurrent = view.findViewById(R.id.tvWeekCurrent);
        rdWeekly = view.findViewById(R.id.rdWeekly);
        rdMonthly = view.findViewById(R.id.rdMonthly);
        rdYearly = view.findViewById(R.id.rdYearly);
        tvTotalCheckIn = view.findViewById(R.id.tvTotalCheckIn);
        tvTotalCheckOut = view.findViewById(R.id.tvTotalCheckOut);
        tvWMY = view.findViewById(R.id.tvWMY);
        tvNewBooking = view.findViewById(R.id.tvNewBooking);

        barEntriesList = new ArrayList<>();
        statisticsWeek();

        rdWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStatisticsByWeek();
            }
        });
        rdMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStatisticsByMonth();
            }
        });
        rdYearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStatisticsByYear();
            }
        });
        return view;
    }

    private void onClickStatisticsByYear() {
        statisticsYear();
    }

    private void onClickStatisticsByMonth() {
        statisticsMonth();
    }

    private void onClickStatisticsByWeek() {
        statisticsWeek();
    }

    private void statisticsWeek() {
        timeRoomBookedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int occupiedValue = 0, maintenanceValue = 0;
                Calendar currentCalendar = Calendar.getInstance();
                int weekCurrent = currentCalendar.get(Calendar.WEEK_OF_YEAR);
                tvWeekCurrent.setText(String.valueOf(weekCurrent));
                tvWMY.setText(String.valueOf("Week no."));
                for (DataSnapshot typeRoomSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot idRoomSnapshot : typeRoomSnapshot.getChildren()) {
                        for (DataSnapshot timeSnapshot : idRoomSnapshot.getChildren()) {
                            String time = timeSnapshot.getValue(String.class);

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                assert time != null;
                                Date date = dateFormat.parse(time);
                                assert date != null;
                                calendar.setTime(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                            if (weekOfYear == weekCurrent) {
                                occupiedValue++;
                            }
                        }
                    }
                }
                setPieChart(100 * occupiedValue / (7 * 18),
                        100 - 100 * occupiedValue / (7 * 18), maintenanceValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        infoRoomBookedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BarChartItem[] barChartItems = new BarChartItem[20];
                for (int i = 0; i < barChartItems.length; i++) {
                    barChartItems[i] = new BarChartItem();
                }
                int totalCheckIn = 0, totalCheckOut = 0;
                int index = 0;
                for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                    int count = 0;
                    for (DataSnapshot infoRoomBooked : roomSnapshot.getChildren()) {
                        InfoRoomBookedModel infoRoomBookedModel = (InfoRoomBookedModel) infoRoomBooked.getValue(InfoRoomBookedModel.class);
                        Calendar currentCalendar = Calendar.getInstance();
                        int weekCurrent = currentCalendar.get(Calendar.WEEK_OF_YEAR);
                        if (infoRoomBookedModel != null) {
                            String StringDateArrive = infoRoomBookedModel.getDateArrive();
                            String StringDateLeave = infoRoomBookedModel.getDateLeave();
                            Calendar calendarArrive = Calendar.getInstance();
                            Calendar calendarLeave = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                Date dateArrive = dateFormat.parse(StringDateArrive);
                                Date dateLeave = dateFormat.parse(StringDateLeave);
                                calendarArrive.setTime(dateArrive);
                                calendarLeave.setTime(dateLeave);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (weekCurrent == calendarArrive.get(Calendar.WEEK_OF_YEAR)) {
                                totalCheckIn++;
                                count++;
                            }
                            if (weekCurrent == calendarLeave.get(Calendar.WEEK_OF_YEAR)) {
                                totalCheckOut++;
                            }
                        }
                    }
                    barChartItems[index].setHeight(count);
                    barChartItems[index].setRoomId(roomSnapshot.getKey());
                    index++;
                }
                tvTotalCheckIn.setText(String.valueOf(totalCheckIn));
                tvTotalCheckOut.setText(String.valueOf(totalCheckOut));
                tvNewBooking.setText(String.valueOf(totalCheckIn));
                setValueBarChart(barChartItems, index);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void statisticsMonth() {
        timeRoomBookedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int occupiedValue = 0, maintenanceValue = 0;
                Calendar currentCalendar = Calendar.getInstance();
                int monthCurrent = currentCalendar.get(Calendar.MONTH);
                tvWeekCurrent.setText(String.valueOf(monthCurrent + 1));
                tvWMY.setText(String.valueOf("Month no."));
                for (DataSnapshot typeRoomSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot idRoomSnapshot : typeRoomSnapshot.getChildren()) {
                        for (DataSnapshot timeSnapshot : idRoomSnapshot.getChildren()) {
                            String time = timeSnapshot.getValue(String.class);
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                assert time != null;
                                Date date = dateFormat.parse(time);
                                assert date != null;
                                calendar.setTime(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            int monthOfYear = calendar.get(Calendar.MONTH);
                            if (monthOfYear == monthCurrent) {
                                occupiedValue++;
                            }
                        }
                    }
                }
                Calendar cal = Calendar.getInstance();
                int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                setPieChart(100 * occupiedValue / (daysInMonth * 18),
                        100 - 100 * occupiedValue / (daysInMonth * 18), maintenanceValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        infoRoomBookedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BarChartItem[] barChartItems = new BarChartItem[20];
                for (int i = 0; i < barChartItems.length; i++) {
                    barChartItems[i] = new BarChartItem();
                }
                int totalCheckIn = 0, totalCheckOut = 0;
                int index = 0;
                for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                    int count = 0;
                    for (DataSnapshot infoRoomBooked : roomSnapshot.getChildren()) {
                        InfoRoomBookedModel infoRoomBookedModel = (InfoRoomBookedModel) infoRoomBooked.getValue(InfoRoomBookedModel.class);
                        Calendar currentCalendar = Calendar.getInstance();
                        int weekCurrent = currentCalendar.get(Calendar.MONTH);
                        if (infoRoomBookedModel != null) {
                            String StringDateArrive = infoRoomBookedModel.getDateArrive();
                            String StringDateLeave = infoRoomBookedModel.getDateLeave();
                            Calendar calendarArrive = Calendar.getInstance();
                            Calendar calendarLeave = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                Date dateArrive = dateFormat.parse(StringDateArrive);
                                Date dateLeave = dateFormat.parse(StringDateLeave);
                                calendarArrive.setTime(dateArrive);
                                calendarLeave.setTime(dateLeave);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (weekCurrent == calendarArrive.get(Calendar.MONTH)) {
                                totalCheckIn++;
                                count++;
                            }
                            if (weekCurrent == calendarLeave.get(Calendar.MONTH)) {
                                totalCheckOut++;
                            }
                        }
                    }
                    barChartItems[index].setHeight(count);
                    barChartItems[index].setRoomId(roomSnapshot.getKey());
                    index++;
                }
                tvTotalCheckIn.setText(String.valueOf(totalCheckIn));
                tvTotalCheckOut.setText(String.valueOf(totalCheckOut));
                tvNewBooking.setText(String.valueOf(totalCheckIn));
                setValueBarChart(barChartItems, index);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void statisticsYear() {
        timeRoomBookedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int occupiedValue = 0, maintenanceValue = 0;
                Calendar currentCalendar = Calendar.getInstance();
                int yearCurrent = currentCalendar.get(Calendar.YEAR);
                tvWeekCurrent.setText(String.valueOf(yearCurrent));
                tvWMY.setText(String.valueOf("Year no."));
                for (DataSnapshot typeRoomSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot idRoomSnapshot : typeRoomSnapshot.getChildren()) {
                        for (DataSnapshot timeSnapshot : idRoomSnapshot.getChildren()) {
                            String time = timeSnapshot.getValue(String.class);

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                assert time != null;
                                Date date = dateFormat.parse(time);
                                assert date != null;
                                calendar.setTime(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            int weekOfYear = calendar.get(Calendar.YEAR);
                            if (weekOfYear == yearCurrent) {
                                occupiedValue++;
                            }

                        }
                    }
                }
                Calendar cal = Calendar.getInstance();
                int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
                setPieChart(100 * occupiedValue / (daysInMonth * 18),
                        100 - 100 * occupiedValue / (daysInMonth * 18), maintenanceValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        infoRoomBookedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BarChartItem[] barChartItems = new BarChartItem[20];
                for (int i = 0; i < barChartItems.length; i++) {
                    barChartItems[i] = new BarChartItem();
                }
                int totalCheckIn = 0, totalCheckOut = 0;
                int index = 0;
                for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                    int count = 0;
                    for (DataSnapshot infoRoomBooked : roomSnapshot.getChildren()) {
                        InfoRoomBookedModel infoRoomBookedModel = (InfoRoomBookedModel) infoRoomBooked.getValue(InfoRoomBookedModel.class);
                        Calendar currentCalendar = Calendar.getInstance();
                        int yearCurrent = currentCalendar.get(Calendar.YEAR);
                        if (infoRoomBookedModel != null) {
                            String StringDateArrive = infoRoomBookedModel.getDateArrive();
                            String StringDateLeave = infoRoomBookedModel.getDateLeave();
                            Calendar calendarArrive = Calendar.getInstance();
                            Calendar calendarLeave = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                Date dateArrive = dateFormat.parse(StringDateArrive);
                                Date dateLeave = dateFormat.parse(StringDateLeave);
                                calendarArrive.setTime(dateArrive);
                                calendarLeave.setTime(dateLeave);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (yearCurrent == calendarArrive.get(Calendar.YEAR)) {
                                totalCheckIn++;
                                count++;
                            }
                            if (yearCurrent == calendarLeave.get(Calendar.YEAR)) {
                                totalCheckOut++;
                            }
                        }
                    }
                    barChartItems[index].setHeight(count);
                    barChartItems[index].setRoomId(roomSnapshot.getKey());
                    index++;
                }
                tvTotalCheckIn.setText(String.valueOf(totalCheckIn));
                tvTotalCheckOut.setText(String.valueOf(totalCheckOut));
                tvNewBooking.setText(String.valueOf(totalCheckIn));
                setValueBarChart(barChartItems, index);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setPieChart(int occupiedValue, int availableValue, int maintenanceValue) {
        String s1 = occupiedValue + "%";
        String s2 = availableValue + "%";
        tvOccupiedValue.setText(s1);
        tvAvailableValue.setText(s2);
        tvMaintenanceValue.setText("0%");
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        PieEntry occupiedEntry = new PieEntry(occupiedValue);
        PieEntry availableEntry = new PieEntry(availableValue);
        PieEntry maintenanceEntry = new PieEntry(maintenanceValue);

        pieEntries.add(occupiedEntry);
        pieEntries.add(availableEntry);
        pieEntries.add(maintenanceEntry);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");

        int[] colors = {Color.parseColor("#FF4081"),
                Color.parseColor("#00C853"), Color.parseColor("#9E9E9E")};
        pieDataSet.setColors(colors);
        pieDataSet.setDrawValues(false);
        pieChart.setHoleRadius(50f);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.animateXY(3000, 3000);
        pieChart.getDescription().setEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
    }

    private void setValueBarChart(BarChartItem[] list, int index) {
        Arrays.sort(list);
        barEntriesList.clear();
        for (int i = 0; i < 9; i++) {
            if (i < index) {
                BarChartItem barChartItem = list[i];
                barEntriesList.add(new BarEntry(i, barChartItem.getHeight()));
            } else {
                barEntriesList.add(new BarEntry(i, 5));
            }
        }
        setUpChart(list);
    }
    private void setUpChart(BarChartItem[] list) {
        BarDataSet barDataSet = new BarDataSet(barEntriesList, "");
        barDataSet.setColor(Color.parseColor("#799BF9"));
        barDataSet.setDrawValues(false);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
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

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return list[(int) value].getRoomId();
            }
        });

        barChart.invalidate();
    }
}