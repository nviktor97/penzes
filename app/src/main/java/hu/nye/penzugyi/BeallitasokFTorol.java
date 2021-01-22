package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class BeallitasokFTorol extends AppCompatActivity {

    Button Torol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beallitasok_f_torol);

        Torol = findViewById(R.id.TOROL_OK);
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);

        Torol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Torol torol = new Torol();
                torol.execute(sharedPreferences.getString("email",""));
            }
        });

    }
    private class Torol extends AsyncTask<String, Void, String>{
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("0")){
                Toast.makeText(getApplicationContext(),"Sikerült a törlés!",Toast.LENGTH_SHORT).show();
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.Belepes.class);
                startActivity(Hiv);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Nem sikerült a törlés!",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String email = strings[0];
            HttpURLConnection conn;
            URL url;
            StringBuffer result = new StringBuffer();
            try {
                url = new URL("https://studentlab.nye.hu/~penzugyi/torol.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                OutputStreamWriter ki = new OutputStreamWriter(conn.getOutputStream());
                ki.write("email="+email);
                ki.flush();
                ki.close();
                if( conn.getResponseCode() == HttpURLConnection.HTTP_OK ){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String sor;
                    while((sor=bufferedReader.readLine())!=null) {
                        result.append(sor);
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }
    }
}