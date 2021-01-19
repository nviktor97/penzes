package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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

public class Belepes extends AppCompatActivity {

    Button Belepes;
    Button Regisztracio;
    Switch BejelentkezveM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BejelentkezveM = findViewById(R.id.SwitchMarad);
        final EditText email = findViewById(R.id.editTextBejEmail);
        final EditText jelszo = findViewById(R.id.editTextBejPassword);
        Belepes = findViewById(R.id.ButtonBE);
        Belepes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Sikeres bejelentkezés!",Toast.LENGTH_SHORT).show();
                //Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.Fooldal.class);
                //startActivity(Hiv);
                String email_sz = email.getText().toString();
                String jelszo_sz = jelszo.getText().toString();
                if(email_sz.isEmpty() || jelszo_sz.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Minden mező kitöltése kötelező!",Toast.LENGTH_SHORT).show();
                }else {
                    Belep belep = new Belep();
                    belep.execute(email_sz, jelszo_sz);
                }
            }
        });

        Regisztracio =findViewById(R.id.ButtonREG);
        Regisztracio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.Regisztracio.class);
                startActivity(Hiv);
            }
        });

    }
    private class Belep extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            s = s.trim();
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            if(s.equals("0")){
                Toast.makeText(getApplicationContext(),"Sikeres bejelentkezés!",Toast.LENGTH_SHORT).show();
                final EditText email = findViewById(R.id.editTextBejEmail);
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.Fooldal.class);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("email", email.getText().toString());
                editor.commit();
                startActivity(Hiv);
            }else if(s.equals("1")){
                Toast.makeText(getApplicationContext(),"Hibás jelszó!",Toast.LENGTH_SHORT).show();
            }else if(s.equals("2")){
                Toast.makeText(getApplicationContext(),"Nincs ilyen email regisztrálva!",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String email = strings[0];
            String jelszo = strings[1];
            StringBuffer result = new StringBuffer();
            HttpURLConnection conn;
            URL url;
            try{
                url = new URL("https://studentlab.nye.hu/~penzugyi/login.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                OutputStreamWriter ki = new OutputStreamWriter(conn.getOutputStream());
                ki.write("email="+email+"&password="+jelszo);
                ki.flush();
                ki.close();
                if( conn.getResponseCode() == HttpURLConnection.HTTP_OK ){
                }else{
                    InputStream err = conn.getErrorStream();
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String sor;
                while((sor=bufferedReader.readLine())!=null) {
                    result.append(sor + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }
    }
}

