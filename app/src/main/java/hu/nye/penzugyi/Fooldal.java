package hu.nye.penzugyi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import java.util.ArrayList;

public class Fooldal extends AppCompatActivity {

    FloatingActionButton ButtonBeallit;
    FloatingActionButton ButtonBevHozz;
    FloatingActionButton ButtonKiadHozz;
    FloatingActionButton ButtonKilep;
    Button bevetel;
    Button kiadas;
    TextView nev;
    TextView egyenleg;

    String userName;
    String userEmail;
    String userBalance;
    Integer userID;
    Integer userIncome = 0;
    Integer userOutcome = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButtonBeallit= findViewById(R.id.FButtonBeallit);
        ButtonBevHozz= findViewById(R.id.FButtonBevHozz);
        ButtonKiadHozz= findViewById(R.id.FButtonKiadHozz);
        ButtonKilep= findViewById(R.id.FButtonKilep);
        bevetel= findViewById(R.id.buttonBevetel);
        kiadas= findViewById(R.id.buttonKiadas);
        nev= findViewById(R.id.username);
        egyenleg= findViewById(R.id.balance);

        ButtonBeallit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.BeallitasokMenu.class);
                startActivity(Hiv);
            }
        });

        ButtonBevHozz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.TetelHozzaadas.class);
                startActivity(Hiv);
            }
        });

        ButtonKiadHozz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.TetelHozzaadas.class);
                startActivity(Hiv);
            }
        });

        ButtonKilep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.Belepes.class);
                startActivity(Hiv);
            }
        });
        bevetel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.Tetelek.class);
                startActivity(Hiv);
            }
        });

        kiadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.Tetelek.class);
                startActivity(Hiv);
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);
        String semail = pref.getString("email", null);
        if (semail != null) {
            Lekerdezo lekerdezo = new Lekerdezo();
            lekerdezo.execute(semail);
        }

    }

    public class Lekerdezo extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            nev.setText(userName);
            egyenleg.setText(userBalance + " Ft");
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("id", userID);
            editor.commit();
            LekerTetelek lekerTetelek = new LekerTetelek();
            lekerTetelek.execute(userID);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String email = strings[0];
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
                ki.write("email=" + email);
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
                    JSONArray posts = jsonObj.getJSONArray("user");

                    // looping through All Contacts
                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject c = posts.getJSONObject(i);
                        String uemail = c.getString("email");
                        String uname = c.getString("name");
                        String ubalance = c.getString("balance");
                        Integer uid = c.getInt("id");
                        // adding contact to contact list
                        Log.e("SAJAT", "adatok: " + uemail + " " + uname + " " + ubalance + " " + uid);
                        userName = uname;
                        userEmail = uemail;
                        userBalance = ubalance;
                        userID = uid;
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

    public class LekerTetelek extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            bevetel.setText("Bevételek összesen: " + userIncome + " Ft");
            kiadas.setText("Kiadások összesen: " + userOutcome + " Ft");
        }

        @Override
        protected Void doInBackground(Integer... strings) {
            Integer id = strings[0];
            HttpURLConnection con=null;
            URL url = null;
            String valasz = null;

            try {
                url= new URL("https://studentlab.nye.hu/~penzugyi/getUserData.php");
                con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestMethod("POST");

                OutputStreamWriter ki = new OutputStreamWriter(con.getOutputStream());
                ki.write("id="+id+"&funkcio=tetelek");
                ki.flush();
                ki.close();

                if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
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
                        if(type.equals("BEVETEL")){
                            userIncome += value;
                        }else{
                            userOutcome += value;
                        }
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
}
