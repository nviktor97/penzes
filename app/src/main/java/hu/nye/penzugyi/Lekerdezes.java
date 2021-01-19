package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PrivateKey;
import java.util.Calendar;

public class Lekerdezes extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "Lekerdezes";
    private TextView mDisplayDate1;
    private TextView mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateSetListener1;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    private Button buttonLekerdezes;
    private RadioButton radioBevetel;
    private RadioButton radioKiadas;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Integer UserID;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lekerdezes);

        spinner = findViewById(R.id.spinnerKategoria);
        adapter = ArrayAdapter.createFromResource(this, R.array.Kategoriak_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        radioBevetel = findViewById(R.id.radioBevetel);
        radioKiadas = findViewById(R.id.radioKiadas);

        radioGroup = findViewById(R.id.tipusGroup);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);
        UserID = pref.getInt("id", 0);

        buttonLekerdezes = findViewById(R.id.buttonLekerdezes);
        buttonLekerdezes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lekerdezes.LekerTetelek lekerTetelek = new Lekerdezes.LekerTetelek();

                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);

                String ID = UserID.toString();
                String tol = mDisplayDate1.getText().toString();
                String ig = mDisplayDate2.getText().toString();
                String tipus = String.valueOf(radioGroup.indexOfChild(radioButton));
                String kat = String.valueOf(adapter.getPosition(spinner.getSelectedItem().toString()));

                lekerTetelek.execute(ID, tol, ig, tipus, kat);
            }
        });

        mDisplayDate1 = findViewById(R.id.textViewDateTOL);
        mDisplayDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Lekerdezes.this,
                        android.R.style.Theme_DeviceDefault,
                        mDateSetListener1,
                        year, month, day);
                dialog.show();
            }
        });
        mDisplayDate2 = findViewById(R.id.textViewDateIG);
        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Lekerdezes.this,
                        android.R.style.Theme_DeviceDefault,
                        mDateSetListener2,
                        year, month, day);
                dialog.show();
            }
        });
        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                mDisplayDate1.setText(date);
            }
        };
        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                mDisplayDate2.setText(date);
            }
        };
    }

    public class LekerTetelek extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

        @Override
        protected Void doInBackground(String... strings) {
            String id = strings[0];
            String tol = strings[1];
            String ig = strings[2];
            String tipus = strings[3];
            String kategoria = strings[4];

            Log.d("SAJAT", id + " " + tol + " " + ig + " " + tipus + " " + kategoria);

            HttpURLConnection con = null;
            URL url = null;
            String valasz = null;

            try {
                url = new URL("https://studentlab.nye.hu/~penzugyi/getUserData.php");
                con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestMethod("POST");

                OutputStreamWriter ki = new OutputStreamWriter(con.getOutputStream());
                ki.write("id=" + id + "&funkcio=tetelek");
                ki.flush();
                ki.close();

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("SAJAT", "Válasz kód rendben");
                }

                InputStream in = new BufferedInputStream(con.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();

                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                valasz = sb.toString();
                Log.e("SAJAT", "Response from url: " + valasz);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (valasz != null) {
                try {
                    JSONObject jsonObj = new JSONObject(valasz);

                    // Getting JSON Array node
                    JSONArray posts = jsonObj.getJSONArray("items");

                    // looping through All Contacts
                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject c = posts.getJSONObject(i);
                        String name = c.getString("name");
                        Integer value = c.getInt("value");
                        String type = c.getString("type");
                        String date = c.getString("date");
                        String category = c.getString("category");
                        // adding contact to contact list
                        Log.e("SAJAT", "adatok: " + name + " " + value + " " + type);

                    }
                } catch (final JSONException e) {
                    Log.e("SAJAT", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e("SAJAT", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}