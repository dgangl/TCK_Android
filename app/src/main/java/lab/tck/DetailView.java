package lab.tck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Backend.Entry;
import Interfaces.MyBooleanCompletion;

public class DetailView extends AppCompatActivity {
    TextView typeText;
    TextView dateText;
    TextView timeText;
    TextView platzText;
    Button confirm;

    Entry mainEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        List<Long> l = new ArrayList<>();
        l.add(3l);
        l.add(4l);

        mainEntry = new Entry(new Date(), "FUNKTIONSSPIEL", null, 2, true, l, null, "I moch di Fertig!");

        setFields();
        updateFields();
        setOnClickListeners();

    }

    private void setFields(){
        typeText = findViewById(R.id.type_text);
        dateText = findViewById(R.id.date_text);
        timeText = findViewById(R.id.time_text);
        platzText = findViewById(R.id.platz_text);
        confirm = findViewById(R.id.button2);
    }

    private void updateFields(){

        if(mainEntry == null){
            return;
        }

        typeText.setText(mainEntry.getType());
        dateText.setText(mainEntry.getDateString());


        //Start and end-time
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        int start_time = Integer.valueOf(sdf.format(mainEntry.getDatum()));
        Double d = mainEntry.getDauer();

        int end_time = start_time + d.intValue();

        timeText.setText(start_time + ":00 - "+end_time + ":00 Uhr");

        //Set Places
        String plaetze = "Plätze: ";
        plaetze = plaetze + mainEntry.getPlatz().get(0);

        for (int i = 1; i < mainEntry.getPlatz().size(); i++) {
            plaetze = plaetze + ", " + mainEntry.getPlatz().get(i);
        }

        platzText.setText(plaetze);
    }

    private void setOnClickListeners(){
        typeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: Call the Type Edit View
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: Call the Date Edit View
            }
        });

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: Call the Time Edit View
            }
        });

        platzText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: Call the Platz Edit View
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                mainEntry.uploadToDatabase(new MyBooleanCompletion() {
                    @Override
                    public void onCallback(boolean bool) {
                        System.out.println("Uploaded successfully");
                    }
                });
                */

                System.out.println("Finished Clicked");
                finish();
                startActivity(new Intent(DetailView.this, MainActivity.class));
            }
        });
    }
}
