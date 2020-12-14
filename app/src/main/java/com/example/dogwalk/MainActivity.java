package com.example.dogwalk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText name;
    private Button click_button;
    private TextView result_address;
    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seteri();


        //onclicklistener za dugme click
        click_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseAccess.open();

                String n = name.getText().toString(); // dodjeljuje unijetu vrijdnost editTexta promjenjivoj/objektu "n"
                String funk = databaseAccess.getTime(n);

                result_address.setText("Total time of walk per day: " + funk);

                databaseAccess.close();
            }
        });
    }

    private void seteri() { //  Funkcija koja sluzi za podesavanje vrijednosti svih promenjivi/objekata
        name = findViewById(R.id.name);
        click_button = findViewById(R.id.click_button);
        result_address = findViewById(R.id.result);
        databaseAccess = DatabaseAccess.getInstance(this); // Koristimo "getApplicationContext()" kad pozivimao iz npr OnClick a u suprotnom mozemoda korstimo "this" ili "MainActivity.this"
    }
}