package com.example.dogwalk;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private DatabaseAccess databaseAccess;
    // 14.10.2020
    private CalendarView calendar;
    private SharedPreferences pref, podesavanjaPrefs, setnjePrefs;
    private static String ImePref = "base";
    private ListView nasaLista;
    private int BrDana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); // Nakon ove linije moze se  definisati Svoje objekte user interfejsa
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseAccess = DatabaseAccess.getInstance(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = upitZaPsa();
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // 14.10.2020
        pref = getSharedPreferences(ImePref, Context.MODE_PRIVATE);
        podesavanjaPrefs = getSharedPreferences(Settings.prefsPodesavanja, Context.MODE_PRIVATE);
        setnjePrefs = getSharedPreferences(AddTimeOfWalk.BazaZavremeSetnji, Context.MODE_PRIVATE);
        int brPrivremeno = podesavanjaPrefs.getInt(Settings.BrSetnjiPodesavanja, 0);
        if (brPrivremeno != 0) {
            BrDana = brPrivremeno;
        } else {
            Intent intent = new Intent(MainActivity2.this, Settings.class);
            intent.putExtra("false", "1");
            startActivity(intent);
            finish();
        }

        //   Editor edit = pref.edit();
        //   edit.putString("setnje", "20201020-120-3");

        // for () { // postavis gornja granica da bude 7 dana
        // ode cijelog datuma "20201020" ti tebas da prevorsi dan u int i da njega povecavas u foru
        // Sting neki = ""

        //  edit.putString("20201020", "120-3"); psotiv key da je datum
        // }

        // kako da isprazinis shared preference
        //   edit.commit();
        calendar = findViewById(R.id.calendarView);
        nasaLista = findViewById(R.id.nasalista);
        nasaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = view.findViewById(R.id.datum);

                Intent prelaz = new Intent(MainActivity2.this, AddTimeOfWalk.class);
                prelaz.putExtra("datum", text.getText().toString());
                prelaz.putExtra("pozicija", position);
                prelaz.putExtra("brelemenata", nasaLista.getAdapter().getCount());
                startActivity(prelaz);
                finish();
            }
        });


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //Toast.makeText(MainActivity2.this,  year + " " + (month + 1) + " " + dayOfMonth, Toast.LENGTH_LONG).show();
                String datum = String.valueOf(year) + String.valueOf(month + 1) + String.valueOf(dayOfMonth);
                String setnje = pref.getString(datum, "");
                String setnjeVrijeme = setnjePrefs.getString(datum, "");
                ArrayList<String> mojStr = new ArrayList<String>();
                ArrayList<String> vrijeme = new ArrayList<String>();
                if (!setnje.equals("")) {
                    String[] arr = setnje.split("-");
                    int vrijemeInt = Integer.parseInt(arr[0]);
                    String BrSetnja = arr[1];
                    int vrijemeH = vrijemeInt / 60;
                    int vrijemeMin = vrijemeInt % 60;

                    for (int i = 0; i < Integer.valueOf(BrSetnja); i++) {
                        int provjera = vrijemeInt - 60;

                        if (vrijemeInt > 0) {
                            String drzac = "";
                            if (provjera > 0) {
                                drzac = "You should walk your dog 1h ";
                                vrijemeInt = vrijemeInt - 60;
                            } else {
                                int provera2 = vrijemeInt - 30;
                                if ((i + 1) != Integer.valueOf(BrSetnja)) {
                                    if (provera2 > 0) {
                                        drzac = "You should walk your dog 30min ";
                                        vrijemeInt = vrijemeInt - 30;
                                    } else {
                                        drzac = "You should walk your dog 15min ";
                                        vrijemeInt = vrijemeInt - 15;
                                    }
                                } else {
                                    if (provera2 >= 0) {
                                        drzac = "You should walk your dog 30min ";
                                        vrijemeInt = vrijemeInt - 30;
                                    } else {
                                        drzac = "You should walk your dog 15min ";
                                        vrijemeInt = vrijemeInt - 15;
                                    }
                                }
                            }
                            mojStr.add(drzac);
                            if (!setnjeVrijeme.equals("")) {
                                // 0-xx:xx/1-xx:xx
                                String[] arr3 = setnjeVrijeme.split("/");
                                if (arr3.length > i) {
                                    vrijeme.add("Time: " + arr3[i]);
                                }
                            } else {
                                vrijeme.add("Time: xx:xx");
                            }
                          //  Toast.makeText(MainActivity2.this, setnjeVrijeme, Toast.LENGTH_LONG).show();

                        }
                    }
                } else {
                    mojStr.add("Doesn't exist in base");
                    //  Toast.makeText(MainActivity2.this, "ne postoji u bazi", Toast.LENGTH_LONG).show();
                }
                OurAdapter adapter = new OurAdapter(MainActivity2.this, mojStr, vrijeme, datum);
                nasaLista.setAdapter(adapter); // Ovde podesavamo vrednosti nase liste
            }
        });

    }

    private AlertDialog.Builder upitZaPsa() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Enter dog's breed");
        LayoutInflater inf = this.getLayoutInflater();
        View dialogView = inf.inflate(R.layout.activity_main, null);
        dialogBuilder.setView(dialogView);

        Button click = dialogView.findViewById(R.id.click_button);
        click.setVisibility(View.GONE); // u svrhe demostracije brobaj za domaci da promenis GONE u Invisible da vidis sta se desi
        final EditText name = dialogView.findViewById(R.id.name);
        dialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseAccess.open();

                String n = name.getText().toString(); // dodeljuje unetu vrednost editTexta promenjivoj/objektu "n"
                String funk = databaseAccess.getTime(n);
                String raw = databaseAccess.getTimeRaw(n);

                databaseAccess.close();
                AlertDialog.Builder builder = odgovrnaUpit("You should walk your dog for: " + funk, raw);
                AlertDialog dialogOdgovor = builder.create();
                dialogOdgovor.show();
                //   dialog.dismiss();
            }
        });
        return dialogBuilder;
    }

    private AlertDialog.Builder odgovrnaUpit(String vrednost, final String raw) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Suggested metrics for your breed are:");
        dialogBuilder.setMessage(vrednost);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //    dialog.dismiss();
            }
        });
        dialogBuilder.setNeutralButton("Put data in calendar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dodajKalendar(BrDana, raw);
                    }
                }
        );
        return dialogBuilder;
    }

    private void dodajKalendar(int dani, String parametri) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // 12-DEC-2020 // 2020-12-21 = yyyy-mm-dd
        String formattedDate = df.format(c);
        //  Toast.makeText(MainActivity2.this, formattedDate, Toast.LENGTH_LONG).show();
        String[] vreme = formattedDate.split("-");// 0= godina , 1 = mesec, 2 = dan
        String punDatum = vreme[0] + vreme[1];

        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        Map<String, ?> allEntries = setnjePrefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
           // Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            String[] bufer = {};
            String param = setnjePrefs.getString(entry.getKey(), "");
            if (!param.equals("")) {
                bufer= param.split("/");
            }
            for (int i = 0; i < bufer.length; i++) {
                cancelNotification(Integer.valueOf(entry.getKey()) + i);
            }

          //  cancelNotification(Integer.valueOf(entry.getValue().toString()));
        }
        Editor editor2 = setnjePrefs.edit();
        editor2.clear();
        editor2.commit();

        // TODO odrati provjere ako je 30 u mesecu da ne prodje dalje
        for (int i = 1; i <= dani; i++) {

            String privremeno = punDatum + (Integer.valueOf(vreme[2]) + i);
            editor.putString(privremeno, parametri);
        }
        editor.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meni, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity2.this, Settings.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void cancelNotification(int datum) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", "Reminder to walk your dog");
        intent.putExtra("text", "Today, you have a walk with your dog");
        PendingIntent pending = PendingIntent.getBroadcast(this, datum, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pending);
    }

}