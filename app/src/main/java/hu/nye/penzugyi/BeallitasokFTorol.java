package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BeallitasokFTorol extends AppCompatActivity {

    Button Torol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beallitasok_f_torol);

        Torol = findViewById(R.id.TOROL_OK);

        Torol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MAHiv = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(MAHiv);
            }
        });

    }
}