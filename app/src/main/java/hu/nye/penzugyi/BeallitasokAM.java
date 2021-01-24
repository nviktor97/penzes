package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
   private EditText Password_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beallitasok_a_m);

        Mentes = findViewById(R.id.ADATM_OK);
        Nev = findViewById(R.id.editTextName);
        Email = findViewById(R.id.editTextEmailcim);
        Password = findViewById(R.id.editTextPasswords);
        Password_new = findViewById(R.id.editTextPasswords2);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);
        Email.setText(pref.getString("email", ""));
        Nev.setText(pref.getString("name", ""));

        Mentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Password.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"Minden mező kitöltése kötelező!",Toast.LENGTH_SHORT).show();
                }else if(Password_new.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"Minden mező kitöltése kötelező!",Toast.LENGTH_SHORT).show();
                }else{
                    Adatmodosit adatmodosit = new Adatmodosit();
                    adatmodosit.execute(Email.getText().toString(),Password.getText().toString(),Password_new.getText().toString());
                }
            }
        });



    }

    private class Adatmodosit extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("0")){
                Toast.makeText(BeallitasokAM.this, "Sikeres adatmódosítás", Toast.LENGTH_SHORT).show();
                BeallitasokAM.this.finish();
            }else{
                Toast.makeText(BeallitasokAM.this, "Nem egyezik a régi jelszavad.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String email = strings[0];
            String pw = strings[1];
            String pw_new = strings[2];
            String result = null;
            HttpURLConnection conn;
            URL url;
            try{
                url = new URL("https://studentlab.nye.hu/~penzugyi/jelszomodosit.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                OutputStreamWriter ki = new OutputStreamWriter(conn.getOutputStream());
                ki.write("email="+email+"&regijelszo="+pw+"&ujjelszo="+pw_new);
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