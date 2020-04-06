package lab.Frontend.New_Reservation.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import backend.Database.Entry;
import backend.Database.Person;
import backend.LocalStorage;
import backend.CompletionTypes.MyBooleanCompletion;
import lab.Frontend.CustomAddMembersDialog;
import lab.Frontend.MainView.MainActivity;
import lab.Frontend.LoadingAnimation;
import lab.Frontend.New_Reservation.Adapter.ChooseMembersAdapter;
import lab.tck.R;

public class DetailView extends AppCompatActivity {
    FloatingActionButton delete;
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
        if (caller != null) { // always null if detailview is for CreateEntry
            confirm.setText(caller);
        } else {
            delete.setEnabled(false);
            delete.hide();
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
        delete = findViewById(R.id.delete);
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
        delete.setOnClickListener(view -> {
            LoadingAnimation l = new LoadingAnimation();
            l.startLoadingAnimation(view.getContext());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Entrys").document(mainEntry.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("WORKED", "DocumentSnapshot successfully deleted!");
                        l.closeLoadingAnimation();
                        finish();
                        sendNotificationsToParticipants(false);
                    })
                    .addOnFailureListener(e -> Log.w("ERROR", "Error deleting document", e));

        });

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

                if (confirm.getText().equals("Verlassen")) {
                    //Todo: Edit current Entry

                    startActivity(new Intent(DetailView.this, MainActivity.class));
                    loadingAnimation.closeLoadingAnimation();

                } else {
                    mainEntry.uploadToDatabase(new MyBooleanCompletion() {
                        @Override
                        public void onCallback(boolean bool) {

                            System.out.println("Uploaded successfully");
                            sendNotificationsToParticipants(true);
                            LocalStorage.creatingEntry = null;
                            startActivity(new Intent(DetailView.this, MainActivity.class));
                            loadingAnimation.closeLoadingAnimation();
                        }
                    });
                }


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

    private void sendNotificationsToParticipants(boolean createEntry) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (Person p : members) {
            DocumentReference docRef = db.collection("Users").document(p.nummer);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && LocalStorage.loadUser().nummer != p.nummer) {
                        // Document found in the offline cache
                        DocumentSnapshot document = task.getResult();

                        String token = document.get("apnToken").toString();
                        String title;
                        String bodyStr;
                        if(createEntry){
                            title = typeText.getText().toString();
                            bodyStr = LocalStorage.loadUser().nachname + " " + LocalStorage.loadUser().vorname + " lädt sie ein beim Event: " + typeText.getText() + " teilzunehmen.";;
                        }else{
                            title= typeText.getText().toString();
                            bodyStr = "Das Event " + typeText.getText() + " wurde gelöscht.";;
                        }


                        try {

                            JSONObject jsonParam = new JSONObject();
                            jsonParam.put("to", token);
                            JSONObject notification = new JSONObject();
                            notification.put("title", title);
                            notification.put("body", bodyStr);
                            jsonParam.put("notification", notification);

                            OkHttpClient client = new OkHttpClient();
                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, jsonParam.toString());
                            Request request = new Request.Builder()
                                    .url("https://fcm.googleapis.com/fcm/send")
                                    .method("POST", body)
                                    .addHeader("Authorization", "key=AAAAIrOpNhM:APA91bE8XG74UZnQ5cVLYesQSCPbtxkD-PmxeLmsmG5n3bsXUmTyMCYntDetW8pp7oG0moobtAx5FVK7jGuKEHEOPg9gYylCYv2LMbXsvuTwqBuwqJbZ3Pv2TwuPvqngzGnaC6z9_IJq")
                                    .addHeader("Content-Type", "application/json")
                                    .build();
                            Response r = client.newCall(request).execute();
                            Log.e("Response!!!", r.toString());
                        } catch (Exception e) {
                            Log.e("ERROR!!!",  e.toString());
                        }

                        Log.e("NUMMER", p.nummer + " : " +  document.get("apnToken").toString());
                    } else {
                        Log.d("FAILED", "OR not sendung to own person!!! || DELETE NOTIFICATION: ", task.getException());
                    }
                }
            });
        }

    }

}
