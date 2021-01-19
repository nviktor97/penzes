package hu.nye.penzugyi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Tetelek extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private TetelAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Integer UserID;
    private Button szures;

    ArrayList<TetelItem> newItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetelek);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        ArrayList<TetelItem> itemlist = new ArrayList<>();
        itemlist.add(new TetelItem("elsőőőő","2020-01-01", "1000 FT", "BEVETEL"));
        mAdapter = new TetelAdapter(itemlist);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        szures = findViewById(R.id.buttonLekerdezes2);
        szures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Hiv = new Intent(getApplicationContext(),hu.nye.penzugyi.Lekerdezes.class);
                startActivity(Hiv);
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Adatok", Context.MODE_PRIVATE);
        UserID = pref.getInt("id", 0);

        Lekerdezo lekerdezo = new Lekerdezo(mAdapter);
        lekerdezo.execute(UserID.toString());

    }

    public class Lekerdezo extends AsyncTask<String, Void, Void> {
        TetelAdapter myAdapter;

        public Lekerdezo(TetelAdapter adapter) {
            this.myAdapter = adapter;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            myAdapter.setNewList(newItemList);

        }

        @Override
        protected Void doInBackground(String... strings) {
            String id = strings[0];
            /*String tol = strings[1];
            String ig = strings[2];
            String tipus = strings[3];
            String kategoria = strings[4];*/
            HttpURLConnection con=null;
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

                    JSONArray posts = jsonObj.getJSONArray("items");

                    // looping through All Contacts
                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject c = posts.getJSONObject(i);
                        String name = c.getString("name");
                        String value = c.getString("value");
                        String type = c.getString("type");
                        String date = c.getString("date");
                        String category = c.getString("category");

                        Log.e("SAJAT", "adatok: " + name + " " + value + " " + type);

                        newItemList.add(new TetelItem(name, date, value, type));
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
