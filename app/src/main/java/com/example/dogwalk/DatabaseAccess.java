package com.example.dogwalk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;


    private DatabaseAccess(Context context) {
        openHelper = new DatabaseOpenHelper(context);
    }


    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }

        return instance;
    }

    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    public String getTimeRaw(String name) {
        name = name.toLowerCase(); // german shepard
        String[] rijeciOdvojene = name.split(" "); // [0]german [1]shepard
        // name = name.substring(0, 1).toUpperCase() + name.substring(1);
        //int tst = rijeciOdvojene.length;
        String privrmeni = "";
        for (int i = 0; i < rijeciOdvojene.length; i++) { // rijeciOdvojene.length ->2
            privrmeni += rijeciOdvojene[i].substring(0, 1).toUpperCase() + rijeciOdvojene[i].substring(1);
            if (i + 1 <  rijeciOdvojene.length) {
                privrmeni += " ";
            }
        }

        Cursor c = db.rawQuery("select Walk_Time, No_Walk from Dogs where Breed = '" + privrmeni + "'", new String[]{});//ucitavamo red koji ukazuje kursor


        if (c.moveToNext()) { // pozivom funkcije moveToNExt() u okvicru Cursor-a dobijamo provjeru dal postoji sledeca vrijednost kao i mijenjanje vrjednosti Cursor-a
            String time = c.getString(0);  // index 0 se ondosi na Walk_Time u nasoj sqllite//vrijeme
            String BrSetnja = c.getString(1); // index 1 se ondosi na No_Walk u nasoj sqllite

            return time + "-" + BrSetnja;//vrijeme
        }else {
            return "False";
        }
    }


    public String getTime(String name) {// German ShePard
        name = name.toLowerCase(); // german shepard
        String[] rijeciOdvojene = name.split(" "); // [0]german [1]shepard
       // name = name.substring(0, 1).toUpperCase() + name.substring(1);
        //int tst = reciOdvojene.length;
        String privrmeni = "";
        for (int i = 0; i < rijeciOdvojene.length; i++) { // reciOdvojene.length ->2
            privrmeni +=  rijeciOdvojene[i].substring(0, 1).toUpperCase() + rijeciOdvojene[i].substring(1);
            if (i + 1 <  rijeciOdvojene.length) {
                privrmeni += " ";
            }
        }


        Cursor c = db.rawQuery("select Walk_Time, No_Walk from Dogs where Breed = '"
                + privrmeni + "'", new String[]{});



        if ( c.moveToNext()) {
        String vrijeme = c.getString(0);  // index 0 se ondosi na Walk_Time u nasoj sqllite
        String BrSetnja = c.getString(1); // index 1 se ondosi na No_Walk u nasoj sqllite


//        StringBuffer buffer2 = new StringBuffer();
        //       while (c2.moveToNext()) {
//            String address = c2.getString(0);
        //         buffer2.append("" + address);
        //      }
        int vrijemeInt = Integer.parseInt(vrijeme);
        int vrijemeH = vrijemeInt / 60;
        int vrijemeMin = vrijemeInt % 60;
        if (vrijemeMin == 0) {
            return vrijemeH + "h, " + BrSetnja + " times a day.";
        } else {
            if (vrijemeH == 0) {
                return vrijemeMin + "min, " + BrSetnja + " times a day.";
            } else {
                return vrijemeH + "h, " + vrijemeMin + "min, " + BrSetnja + " times a day.";
            }
            // return vrijeme + "min," + BrSetnja + "times a day.";


        }
        } else {
            return "False";
        }
    }
}







