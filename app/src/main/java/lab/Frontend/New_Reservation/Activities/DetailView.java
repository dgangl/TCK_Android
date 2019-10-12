package lab.Frontend.New_Reservation.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Backend.Database.Entry;
import Backend.Database.Person;
import Backend.LocalStorage;
import Backend.CompletionTypes.MyBooleanCompletion;
import lab.Frontend.CustomAddMembersDialog;
import lab.Frontend.MainView.MainActivity;
import lab.Frontend.LoadingAnimation;
import lab.Frontend.New_Reservation.Adapter.ChooseMembersAdapter;
import lab.tck.R;

public class DetailView extends AppCompatActivity {
    TextView typeText;
    TextView dateText;
    TextView timeText;
    TextView platzText;
    Button confirm;
    private Button addMember;
    private ListView listViewMembers;
    private List<Person> members = new ArrayList<>();
    private ChooseMembersAdapter adapter;
    Entry mainEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_view);
        setFields();

        String caller = getIntent().getStringExtra("caller");
        if(caller != null){ // always null if detailview is for CreateEntry
            confirm.setText(caller);
        }

        mainEntry = LocalStorage.creatingEntry;
        mainEntry.setType("PRIVATSPIEL");
        mainEntry.setPrivat(true);


        //ListView
        if (mainEntry.getTeilnemer() == null) {
            mainEntry.setTeilnemer(new ArrayList<Person>());
        }
        members.addAll(mainEntry.getTeilnemer());
        adapter = new ChooseMembersAdapter(DetailView.this, android.R.layout.simple_list_item_1, members);
        listViewMembers.setAdapter(adapter);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#021B79"));
        }

        List<Long> l = new ArrayList<>();
        l.add(3l);
        l.add(4l);


        updateFields();
        setOnClickListeners();

    }

    private void setFields() {

        typeText = findViewById(R.id.type_text);
        dateText = findViewById(R.id.date_text);
        timeText = findViewById(R.id.time_text);
        platzText = findViewById(R.id.platz_text);
        confirm = findViewById(R.id.detail_button);
        addMember = findViewById(R.id.detail_addMembers);
        listViewMembers = findViewById(R.id.detail_choosenMembers);

    }

    private void updateFields() {

        if (mainEntry == null) {
            return;
        }

        typeText.setText(mainEntry.getBeschreibung());
        dateText.setText(mainEntry.getDateString());


        //Start and end-time
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        int start_time = Integer.valueOf(sdf.format(mainEntry.getDatum()));
        Double d = mainEntry.getDauer();

        int end_time = start_time + d.intValue();

        timeText.setText(start_time + ":00 - " + end_time + ":00 Uhr");

        //Set Places
        String plaetze = "Platz: ";
        plaetze = plaetze + mainEntry.getPlatz().get(0);

        for (int i = 1; i < mainEntry.getPlatz().size(); i++) {
            plaetze = plaetze + ", " + mainEntry.getPlatz().get(i);
        }

        platzText.setText(plaetze);
    }

    private void setOnClickListeners() {
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
                final LoadingAnimation loadingAnimation = new LoadingAnimation();
                loadingAnimation.startLoadingAnimation(DetailView.this);

                if(confirm.getText().equals("Ändern")){
                    //Todo: Edit current Entry

                }else {
                    mainEntry.uploadToDatabase(new MyBooleanCompletion() {
                        @Override
                        public void onCallback(boolean bool) {

                            System.out.println("Uploaded successfully");

                            loadingAnimation.closeLoadingAnimation();
                            LocalStorage.creatingEntry = null;
                        }
                    });
                }


                startActivity(new Intent(DetailView.this, MainActivity.class));
            }
        });

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAddMembersDialog camd = new CustomAddMembersDialog(DetailView.this, members);
                camd.show();
            }
        });
    }
}
