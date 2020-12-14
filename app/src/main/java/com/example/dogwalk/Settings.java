package com.example.dogwalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class Settings extends AppCompatActivity {

    public static final String prefsPodesavanja = "settings";
    public static final String BrSetnjiPodesavanja = "days";
    private SharedPreferences podesavanjaPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        podesavanjaPrefs = getSharedPreferences(Settings.prefsPodesavanja, Context.MODE_PRIVATE);

        final TextView info = findViewById(R.id.info);
        Intent i = getIntent();

        if (i.hasExtra("false")) {
            info.setText("Can't be 0");
            info.setTextColor(Color.RED);
        }
        final EditText editText = findViewById(R.id.brsetnji);
        editText.setText(String.valueOf(podesavanjaPrefs.getInt(BrSetnjiPodesavanja, 0)));
        Button dugme = findViewById(R.id.button);
        dugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editor edi = podesavanjaPrefs.edit();
                int br = Integer.valueOf(editText.getText().toString());
                edi.putInt(BrSetnjiPodesavanja, br);
                edi.commit();
                info.setText("Successfully changed on: " + br);
                info.setTextColor(Color.GREEN);
                editText.setText("");
            }
        });

        Button testN = findViewById(R.id.button3);
        testN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long trenutno = System.currentTimeMillis();
                trenutno = trenutno + 30000;
                scheduleNotification(Settings.this, trenutno, "In ten minutes, your walk is over!", "test");
            }
        });
    }

    public static void scheduleNotification(Context context, long time, String title, String text) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        PendingIntent pending = PendingIntent.getBroadcast(context, 42, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pending);
    }

    private void notifikacija() {
        int NOTIFICATION_ID = 234;
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title")
                .setContentText("message");

        Intent resultIntent = new Intent(this, MainActivity2.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity2.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Settings.this, MainActivity2.class);
        startActivity(intent);
        finish();
    }
}