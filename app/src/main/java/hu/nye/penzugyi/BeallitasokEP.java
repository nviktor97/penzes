package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

;

public class BeallitasokEP extends AppCompatActivity {

    Button Mentes;
    TextView egyenleg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beallitasok_e_p);

        Mentes = findViewById(R.id.EP_OK);
        egyenleg = findViewById(R.id.editTextEgyenleg);

        Mentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}