package com.example.dogwalk;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OurAdapter extends BaseAdapter {

    private ArrayList<String> kad;
    private ArrayList<String> setnjaVrijeme;
    private String datumi;
    private Activity context;

    public OurAdapter(Activity context, ArrayList<String> kad, ArrayList<String> setnjaVreme, String datumi) {
        this.kad = kad; // this.kad se odnosi na globalnu promenjivu kad a kad sa desne strane jednakosti se odnosi na kad koj je definisan u nasoj funkciji
        this.setnjaVrijeme = setnjaVreme;
        this.datumi = datumi;
        this.context = context;
    }

    @Override
    public int getCount() {
        return kad.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.list_item, parent, false);
        TextView setnja = row.findViewById(R.id.setnja);
        setnja.setText(kad.get(position));

        TextView SetnjaVremeT = row.findViewById(R.id.vremeSetnje);
        if (setnjaVrijeme.size() > position) {
            SetnjaVremeT.setText(setnjaVrijeme.get(position));
        } else {
            SetnjaVremeT.setVisibility(View.GONE);
        }

        TextView datum = row.findViewById(R.id.datum);
        datum.setText(datumi);





        return row;
    }
}
