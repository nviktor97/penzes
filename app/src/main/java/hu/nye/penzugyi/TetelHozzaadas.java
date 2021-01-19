package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class TetelHozzaadas extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "TetelHozzaadas";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button Hozzadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetel_hozzaadas);

        Hozzadas = findViewById(R.id.ButtonHozzad);

        Hozzadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Tétel hozzáadva!",Toast.LENGTH_SHORT).show();
            }
        });

        Spinner spinner = findViewById(R.id.spinnerKategoria);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Kategoriak_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        mDisplayDate = findViewById(R.id.textViewDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TetelHozzaadas.this,
                        android.R.style.Theme_DeviceDefault,
                        mDateSetListener,
                        year, month, day);
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                mDisplayDate.setText(date);
            }
        };

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}