package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Regisztracio extends AppCompatActivity {
    Button ButtonReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisztracio);

        ButtonReg = findViewById(R.id.ButtonREG);

        final EditText nev = findViewById(R.id.textViewRegNev);
        final EditText email = findViewById(R.id.editTextRegEmail);
        final EditText jelszo = findViewById(R.id.editTextRegPassword);


        ButtonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_sz = email.getText().toString();
                String jelszo_sz = jelszo.getText().toString();
                String nev_sz = nev.getText().toString();
                if(email_sz.isEmpty() || jelszo_sz.isEmpty() || nev_sz.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Minden mező kitöltése kötelező!",Toast.LENGTH_SHORT).show();
                }else {
                    Regisztral regisztral = new Regisztral();
                    regisztral.execute(email_sz, jelszo_sz, nev_sz);
                }
            }
        });
    }
    private class Regisztral extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            s = s.trim();
            if(s.equals("0")){
                Toast.makeText(getApplicationContext(),"Sikeres regisztráció!",Toast.LENGTH_SHORT).show();
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.Belepes.class);
                startActivity(Hiv);
            }else if(s.equals("1")){
                Toast.makeText(getApplicationContext(),"HIBA!",Toast.LENGTH_SHORT).show();
            }else if(s.equals("2")){
                Toast.makeText(getApplicationContext(),"Ez az email már regisztrálva van!",Toast.LENGTH_SHORT).show();
            }else if(s.equals("3")){
                Toast.makeText(getApplicationContext(),"Nem valid email!",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String email = strings[0];
            String jelszo = strings[1];
            String nev = strings[2];
            String result = null;
            HttpURLConnection conn;
            URL url;
            try{
                url = new URL("https://studentlab.nye.hu/~penzugyi/register.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                OutputStreamWriter ki = new OutputStreamWriter(conn.getOutputStream());
                ki.write("email="+email+"&password="+jelszo+"&name="+nev);
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

