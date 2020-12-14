package com.example.dogwalk;//package is a library of classes that have already been defined for you.

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTimeOfWalk extends AppCompatActivity {

    public static final String BazaZavremeSetnji = "setnjeBaza";
    private SharedPreferences podesavanjaPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_vreme_setnje);
        podesavanjaPrefs = getSharedPreferences(AddTimeOfWalk.BazaZavremeSetnji, Context.MODE_PRIVATE);
        Intent i = getIntent();
        //  Toast.makeText(AddTimeOfWalk.this, i.getStringExtra("datum") + " " + i.getIntExtra("brelemenata", 0), Toast.LENGTH_LONG).show();
        //  Log.e("TAG-----------TAG", i.getStringExtra("datum"));
        final String datum = i.getStringExtra("datum");
        int brelemenata = i.getIntExtra("brelemenata", 0);
        final int pozicija = i.getIntExtra("pozicija", 0);
        // Toast.makeText(AddTimeOfWalk.this, datum, Toast.LENGTH_LONG).show();

        Button save = findViewById(R.id.button2);
        final EditText vremeEditor = findViewById(R.id.editTextTime);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleNotification(AddTimeOfWalk.this, odrediVreme(datum, vremeEditor.getText().toString()), "Reminder to walk your dog", "Today, you have a walk with your dog", Integer.valueOf(datum + pozicija));
                upisi(datum, vremeEditor.getText().toString(), pozicija);
            }
        });
        String vrednost = podesavanjaPrefs.getString(datum, "nista");
        if (vrednost.equals("nista")) {
            inicijalizuj(datum, brelemenata);
            //  Toast.makeText(AddTimeOfWalk.this, "inicijalizovano", Toast.LENGTH_LONG).show();
        } else {
            // menjamo vec postojece
            //    Toast.makeText(AddTimeOfWalk.this, "ne inic", Toast.LENGTH_LONG).show();
            String[] bufer = vrednost.split("/");
            if (!bufer[pozicija].equals("xx:xx")) {
                vremeEditor.setText(bufer[pozicija]);
            }
        }
    }

    private void upisi(String datum, String vrednost, int pozicija) {
        String drzac = "";
        String ostaliElementi = podesavanjaPrefs.getString(datum, "nista");
        String[] bufer = ostaliElementi.split("/");
        for (int i = 0; i < bufer.length; i++) {
            if (i == pozicija) {
                drzac += vrednost + "/";
            } else {
                drzac += bufer[i] + "/";
            }
        }
        Editor edit = podesavanjaPrefs.edit();
        edit.putString(datum, drzac);
        edit.commit();
        TextView poruka = findViewById(R.id.textView);
        poruka.setText("Added.");
    }

    private void inicijalizuj(String datum, int brElemenata) {
        String drzac = "";
        for (int i = 0; i < brElemenata; i++) {
            drzac += "xx:xx/";
        }
        Editor edit = podesavanjaPrefs.edit();
        edit.putString(datum, drzac);
        edit.commit();
    }

    private long odrediVreme(String datum, String konkretnoVreme) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sati = new SimpleDateFormat("HH:mm");
       // String currentDateandTime = sdf.format(new Date());

        Date date = null;
        try {
            date = sdf.parse(datum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date dateSati = null;
        try {
            dateSati = sati.parse(konkretnoVreme);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dateSati);
       // Log.e("TAG-----------TAG", String.valueOf(calendar.getTimeInMillis()  + calendar2.getTimeInMillis()));
        return calendar.getTimeInMillis() + calendar2.getTimeInMillis();
    }

    private void scheduleNotification(Context context, long time, String title, String text, int datum) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        PendingIntent pending = PendingIntent.getBroadcast(context, datum, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pending);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddTimeOfWalk.this, MainActivity2.class));
        finish();
    }
}