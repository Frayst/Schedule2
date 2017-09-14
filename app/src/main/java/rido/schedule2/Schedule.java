package rido.schedule2;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class Schedule extends AppCompatActivity {

    TextView textView;
    ImageButton imageButton;



    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView)findViewById(R.id.textDate) ;
        imageButton = (ImageButton)findViewById(R.id.imageCalendar) ;
/*
        MaterialCalendarView simpleCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView); // get the reference of CalendarView
        simpleCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(1800, 1, 1))
                .setMaximumDate(CalendarDay.from(2300, 12, 31))
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                                .commit();*/
        /** end after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 1);

        Calendar startDate = Calendar.getInstance();
    startDate.add(Calendar.YEAR, -1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                textView.setText("Selected date: " +date);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CalendarN = new Intent(Schedule.this,CalendarView.class);
                startActivity(CalendarN);
                            }
        });

    }
}
