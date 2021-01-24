package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class BeallitasokAM extends AppCompatActivity {

   Button Mentes;
   private EditText Nev;
   private EditText Email;
   private EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beallitasok_a_m);

        Mentes = findViewById(R.id.ADATM_OK);
        Nev = findViewById(R.id.editTextName);
        Email = findViewById(R.id.editTextEmailcim);
        Password = findViewById(R.id.editTextPasswords);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);
        Email.setText(pref.getString("email", ""));
        Nev.setText(pref.getString("name", ""));

        Mentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nev, email, jelszo, eredeti_email;
                nev = "";
                email = "";
                jelszo = "";
                if(!Nev.getText().toString().equals("")){
                    nev = Nev.getText().toString();
                }
                if(!Email.getText().toString().equals("")){
                    email = Email.getText().toString();
                }
                if(!Password.getText().toString().equals("")){
                    jelszo = Password.getText().toString();
                }
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);
                eredeti_email = pref.getString("email","");
                Adatmodosit adatmodosit = new Adatmodosit();
                adatmodosit.execute(nev,email,jelszo,eredeti_email);
            }
        });



    }

    private class Adatmodosit extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("0")){
                String nev, email;
                nev = "";
                email = "";
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if(!Nev.getText().toString().equals("")){
                    nev = Nev.getText().toString();
                    editor.putString("name", nev);
                }
                if(!Email.getText().toString().equals("")){
                    email = Email.getText().toString();
                    editor.putString("email", email);
                }
                editor.commit();
                Toast.makeText(BeallitasokAM.this, "Sikeres adatmódosítás", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(BeallitasokAM.this, "Sikertelen adatmódosítás", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String nev = strings[0];
            String email = strings[1];
            String jelszo = strings[2];
            String eredeti_email = strings[3];
            String result = null;
            HttpURLConnection conn;
            URL url;
            try{
                url = new URL("https://studentlab.nye.hu/~penzugyi/adatmodosit.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                OutputStreamWriter ki = new OutputStreamWriter(conn.getOutputStream());
                ki.write("nev="+nev+"&email="+email+"&jelszo="+jelszo+"&eredeti_email="+eredeti_email);
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