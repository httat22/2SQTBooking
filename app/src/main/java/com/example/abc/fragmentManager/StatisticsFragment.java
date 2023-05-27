package com.example.abc.fragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.abc.R;
import com.example.abc.models.BarChartItem;
import com.example.abc.models.InfoRoomBookedModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
import java.util.concurrent.TimeUnit;

public class StatisticsFragment extends Fragment {

    private TextView tvMaintenanceValue, tvAvailableValue, tvOccupiedValue, tvWeekCurrent,
            tvTotalCheckOut, tvTotalCheckIn, tvWMY, tvNewBooking, tvSourceBooking, tvPickDateStart,tvPickDateEnd;
    private AppCompatRadioButton rdWeekly, rdMonthly, rdYearly, rdAny;
    private AppCompatButton btnApply;
    private String stringDateStart, stringDateEnd;
    private LinearLayout llCurrentTime;
    private PieChart pieChart;
    private BarChart barChart;
    private CardView cvPickDate;
    private ArrayList<BarEntry> barEntriesList;
    private DatabaseReference timeRoomBookedRef = FirebaseDatabase.getInstance().getReference("time_room_booked");
    private DatabaseReference infoRoomBookedRef = FirebaseDatabase.getInstance().getReference("statistics_room");

    @Override
    public void onStart() {
        super.onStart();
        rdWeekly.setChecked(true);
    }

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
        rdAny = view.findViewById(R.id.rdAny);
        tvTotalCheckIn = view.findViewById(R.id.tvTotalCheckIn);
        tvTotalCheckOut = view.findViewById(R.id.tvTotalCheckOut);
        tvWMY = view.findViewById(R.id.tvWMY);
        tvNewBooking = view.findViewById(R.id.tvNewBooking);
        tvSourceBooking = view.findViewById(R.id.tvSourceBooking);

        cvPickDate = view.findViewById(R.id.cvPickDate);
        btnApply = view.findViewById(R.id.btnApply);
        tvPickDateStart = view.findViewById(R.id.tvPickDateStart);
        tvPickDateEnd = view.findViewById(R.id.tvPickDateEnd);
        llCurrentTime = view.findViewById(R.id.llCurrentTime);

        barEntriesList = new ArrayList<>();
        statisticsWeek();
        rdWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvPickDate.setVisibility(View.GONE);
                llCurrentTime.setVisibility(View.VISIBLE);
                onClickStatisticsByWeek();
            }
        });
        rdMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvPickDate.setVisibility(View.GONE);
                llCurrentTime.setVisibility(View.VISIBLE);
                onClickStatisticsByMonth();
            }
        });
        rdYearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvPickDate.setVisibility(View.GONE);
                llCurrentTime.setVisibility(View.VISIBLE);
                onClickStatisticsByYear();
            }
        });
        rdAny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvPickDate.setVisibility(View.VISIBLE);
                llCurrentTime.setVisibility(View.GONE);
                onClickStatisticsAny();
            }
        });
        return view;
    }

    private void onClickStatisticsAny() {
        pickTimeDuration();
        setPieChart(0, 100, 0);
        tvTotalCheckIn.setText(String.valueOf(0));
        tvTotalCheckOut.setText(String.valueOf(0));
        tvNewBooking.setText(String.valueOf(0));
        tvSourceBooking.setText(String.valueOf(0));
        BarChartItem[] barChartItems = new BarChartItem[100];
        for (int i = 0; i < barChartItems.length; i++) {
            barChartItems[i] = new BarChartItem();
        }
        setValueBarChart(barChartItems, 100);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stringDateEnd!=null && stringDateStart != null) {
                    int numberOfDate = getNumberOfDate(stringDateStart, stringDateEnd) + 1;
                    if (numberOfDate > 0) {
                        statisticsAny();
                    }
                    else {
                        Toast.makeText(getContext(), "End date must be greater than start date!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Choose date for statistics!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                BarChartItem[] barChartItems = new BarChartItem[100];
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
                tvSourceBooking.setText(String.valueOf(totalCheckIn));
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
                BarChartItem[] barChartItems = new BarChartItem[100];
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
                tvSourceBooking.setText(String.valueOf(totalCheckIn));
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
                BarChartItem[] barChartItems = new BarChartItem[100];
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
                tvSourceBooking.setText(String.valueOf(totalCheckIn));
                setValueBarChart(barChartItems, index);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void statisticsAny() {
        timeRoomBookedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int occupiedValue = 0, maintenanceValue = 0;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dateStart = null, dateEnd = null;
                try {
                    dateStart = dateFormat.parse(stringDateStart);
                    dateEnd = dateFormat.parse(stringDateEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (DataSnapshot typeRoomSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot idRoomSnapshot : typeRoomSnapshot.getChildren()) {
                        for (DataSnapshot timeSnapshot : idRoomSnapshot.getChildren()) {
                            String time = timeSnapshot.getValue(String.class);
                            Date date;
                            try {
                                assert time != null;
                                date = dateFormat.parse(time);
                                assert date != null;
                                if (date.compareTo(dateStart) >= 0 && date.compareTo(dateEnd) <= 0) {
                                    occupiedValue++;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                int numberOfDate = getNumberOfDate(stringDateStart, stringDateEnd) + 1;
                setPieChart(100 * occupiedValue / (numberOfDate * 18),
                        100 - 100 * occupiedValue / (numberOfDate * 18), maintenanceValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        infoRoomBookedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dateStart = null, dateEnd = null;
                try {
                    dateStart = dateFormat.parse(stringDateStart);
                    dateEnd = dateFormat.parse(stringDateEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                BarChartItem[] barChartItems = new BarChartItem[100];
                for (int i = 0; i < barChartItems.length; i++) {
                    barChartItems[i] = new BarChartItem();
                }
                int totalCheckIn = 0, totalCheckOut = 0;
                int index = 0;
                for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                    int count = 0;
                    for (DataSnapshot infoRoomBooked : roomSnapshot.getChildren()) {
                        InfoRoomBookedModel infoRoomBookedModel = (InfoRoomBookedModel) infoRoomBooked.getValue(InfoRoomBookedModel.class);
                        if (infoRoomBookedModel != null) {
                            String StringDateArrive = infoRoomBookedModel.getDateArrive();
                            String StringDateLeave = infoRoomBookedModel.getDateLeave();
                            Date dateArrive = null, dateLeave = null;
                            try {
                                dateArrive = dateFormat.parse(StringDateArrive);
                                dateLeave = dateFormat.parse(StringDateLeave);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (dateArrive.compareTo(dateStart) >= 0 && dateArrive.compareTo(dateEnd) <= 0) {
                                totalCheckIn++;
                                count++;
                            }
                            if (dateLeave.compareTo(dateStart) >= 0 && dateLeave.compareTo(dateEnd) <= 0) {
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
                tvSourceBooking.setText(String.valueOf(totalCheckIn));
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
        for (int i = 0; i < 6; i++) {
            if (i < index) {
                BarChartItem barChartItem = list[i];
                barEntriesList.add(new BarEntry(i, barChartItem.getHeight()));
            } else {
                barEntriesList.add(new BarEntry(i, 0));
            }
        }

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return list[(int) value].getRoomId();
            }
        });

        setUpChart(list);
    }
    private void setUpChart(BarChartItem[] list) {
        BarDataSet barDataSet = new BarDataSet(barEntriesList, "");
        barDataSet.setColor(Color.parseColor("#799BF9"));
        barDataSet.setDrawValues(false);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.7f);
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
    public int getNumberOfDate(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long differenceInMilliseconds = endDate.getTime() - startDate.getTime();
        long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds);
        return (int) differenceInDays;
    }
    private void pickTimeDuration() {
        tvPickDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        stringDateStart = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        tvPickDateStart.setText(stringDateStart);
                    }
                }, year, month, day);
                dpd.show(getChildFragmentManager(), "DatePickerDialog");
            }
        });

        tvPickDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        stringDateEnd = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        tvPickDateEnd.setText(stringDateEnd);
                    }
                }, year, month, day);
                dpd.show(getChildFragmentManager(), "DatePickerDialog");
            }
        });
    }
}