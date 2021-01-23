package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BeallitasokAM extends AppCompatActivity {

   Button Mentes;
   private EditText Nev;
   private EditText Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beallitasok_a_m);

        Mentes = findViewById(R.id.ADATM_OK);
        Nev = findViewById(R.id.editTextName);
        Email = findViewById(R.id.editTextEmailcim);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);
        Email.setText(pref.getString("email", ""));
        Nev.setText(pref.getString("name", ""));

        Mentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }

}