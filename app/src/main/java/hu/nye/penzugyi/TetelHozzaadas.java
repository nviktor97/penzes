package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class TetelHozzaadas extends AppCompatActivity  {

    private static final String TAG = "TetelHozzaadas";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button Hozzadas;
    private String tipus;
    private String datum;
    private Integer UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetel_hozzaadas);
        final EditText osszeg = findViewById(R.id.textViewRegNev);
        final EditText nev = findViewById(R.id.textViewRegNev2);

        Hozzadas = findViewById(R.id.ButtonHozzad);

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
                datum = date;
            }
        };

        RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioKiadas:
                        tipus = "Kiadas";
                        break;
                    case R.id.radioBevetel:
                        tipus = "Bevetel";
                        break;
                }
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);
        UserID = pref.getInt("id", 0);

        Hozzadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String datum_sz = datum;
                String osszeg_sz = osszeg.getText().toString();
                String tipus_sz = tipus;
                String nev_sz = nev.getText().toString();
                if(datum_sz == null || osszeg_sz == null || tipus_sz == null || nev_sz == null){
                    Toast.makeText(getApplicationContext(),"Minden mező kitöltése kötelező!",Toast.LENGTH_SHORT).show();
                }else {
                    TetelHozzaadas.TetelHozzaad hozzaad = new TetelHozzaadas.TetelHozzaad();
                    hozzaad.execute(datum_sz, osszeg_sz, tipus_sz, nev_sz, UserID.toString());
                }
            }
        });

    }

    private class TetelHozzaad extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            Log.d("SAJAT", "Válasz:" + s);
            s = s.trim();
            if(s.equals("null")){
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.Fooldal.class);
                startActivity(Hiv);
            }else{
                Toast.makeText(getApplicationContext(),"HIBA!",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String datum = strings[0];
            String osszeg = strings[1];
            String tipus = strings[2];
            String nev = strings[3];
            String userid = strings[4];
            String result = null;
            HttpURLConnection conn;
            URL url;
            try{
                url = new URL("https://studentlab.nye.hu/~penzugyi/addTetel.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                OutputStreamWriter ki = new OutputStreamWriter(conn.getOutputStream());
                ki.write("datum="+datum+"&osszeg="+osszeg+"&tipus="+tipus+"&nev="+nev+"&userid="+userid);
                ki.flush();
                ki.close();
                if( conn.getResponseCode() == HttpURLConnection.HTTP_OK ){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    result = bufferedReader.readLine();
                }else{
                    InputStream err = conn.getErrorStream();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }


}