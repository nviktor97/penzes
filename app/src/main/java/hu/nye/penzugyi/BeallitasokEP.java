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

public class BeallitasokEP extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button Mentes;
    TextView egyenleg;
    Spinner penznem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beallitasok_e_p);

        Mentes = findViewById(R.id.EP_OK);
        egyenleg = findViewById(R.id.editTextEgyenleg);
        penznem = findViewById(R.id.spinnerPenznem);

        Mentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerPenznem, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        penznem.setAdapter(adapter);
        penznem.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}