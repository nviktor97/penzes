package hu.nye.penzugyi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class BeallitasokMenu extends AppCompatActivity {

    Button egyenlegpenznem;
    Button fiokadatok;
    Button fioktorles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beallitasok_menu);

        egyenlegpenznem = findViewById(R.id.BeallitasokEPButt);
        fiokadatok = findViewById(R.id.BeallitasokFAM);
        fioktorles = findViewById(R.id.BeallitasokFTO);


        egyenlegpenznem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EPHiv = new Intent(getApplicationContext(),hu.nye.penzugyi.BeallitasokEP.class);
                startActivity(EPHiv);
            }
        });

        fiokadatok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AMHiv = new Intent(getApplicationContext(),hu.nye.penzugyi.BeallitasokAM.class);
                startActivity(AMHiv);
            }
        });

        fioktorles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FTHiv = new Intent(getApplicationContext(),hu.nye.penzugyi.BeallitasokFTorol.class);
                startActivity(FTHiv);
            }
        });
    }
}