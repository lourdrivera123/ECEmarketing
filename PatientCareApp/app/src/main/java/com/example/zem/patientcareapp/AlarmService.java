package com.example.zem.patientcareapp;

/**
 * Created by Dexter B. on 7/20/2015.
 */

import android.app.AlarmManager;
import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;
import android.app.PendingIntent;

public class AlarmService {
    private PendingIntent pendingIntent;
    Context context;

    public AlarmService(Context context){
        this.context = context;
    }

    public void start() {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int interval = 8000;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(context, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void startAt(int year, int month, int day, int hour, int minutes) { // hours will be in 12 day format; 0 = am, 1 = pm
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 20;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(year, month, day, hour, minutes);
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.AM_PM,hour < 12 ? Calendar.AM : Calendar.PM);


        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, pendingIntent);
    }

}