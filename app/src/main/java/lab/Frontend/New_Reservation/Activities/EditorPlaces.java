package lab.Frontend.New_Reservation.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Backend.Database.BackendFeedDatabase;
import Backend.LocalStorage;
import Backend.Places;
import Backend.CompletionTypes.MyIntArrayCompletion;
import lab.Frontend.New_Reservation.Adapter.ChoosesPlacesAdapter;
import lab.Frontend.ToastMaker;
import lab.tck.R;

public class EditorPlaces extends AppCompatActivity {
    private ListView listViewPlaces;
    private List<Places> places = new ArrayList<>();
    private ChoosesPlacesAdapter adapter;
    private Button buttonAddMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_places);

        //Actionbar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#021B79"));
        }

        //Toolbar
        Toolbar mToolbar =  findViewById(R.id.toolbar2);

        mToolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Button
        buttonAddMembers = findViewById(R.id.editor_addMembers);
        buttonAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlatzToEntry();
                Log.e("??????", LocalStorage.creatingEntry.getPlatz().toString());
                if (LocalStorage.creatingEntry.getPlatz() != null && LocalStorage.creatingEntry.getPlatz().size() > 0) {
                    Intent intent = new Intent(EditorPlaces.this, EditorMembers.class);
                    startActivity(intent);
                } else {
                    ToastMaker tm = new ToastMaker();

                    tm.createToast(EditorPlaces.this, "Bitte wählen Sie einen Platz/Plätze auf welchen Sie spielen möchten!");
                }
            }
        });



        BackendFeedDatabase bfd = new BackendFeedDatabase();
        bfd.freePlace(LocalStorage.creatingEntry.getDatum(), (int) LocalStorage.creatingEntry.getDauer(), new MyIntArrayCompletion() {
            @Override
            public void onCallback(List<Integer> intList) {

                System.out.println("GGA: Loaded Places!" + intList.size());
                //Listview
                listViewPlaces = findViewById(R.id.editor_placeListview);

                if(intList.size() == 0){
                    buttonAddMembers.setText("Datum/Uhrzeit ändern");

                    buttonAddMembers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(EditorPlaces.this, EditorDateAndDurration.class);
                            startActivity(intent);
                        }
                    });

                    TextView headline = findViewById(R.id.editor_textView);
                    headline.setText("Um diese Zeit ist leider kein Platz mehr frei.");
                }
                else {
                    for (Integer i : intList) {
                        places.add(new Places("Platz " + i, false));
                    }
                }
                adapter = new ChoosesPlacesAdapter(EditorPlaces.this, android.R.layout.simple_list_item_1, places);
                listViewPlaces.setAdapter(adapter);


            }
        });






    }

    private void addPlatzToEntry(){

        List<Integer> intList = new ArrayList<>();

        for (Places p : places){
            if(p.isTake()){
                Integer a = Integer.parseInt(p.getPlace().split(" ")[1]);
                intList.add(a);
                Log.e("!!!!!!!!!!", a.toString());
            }

        }

        LocalStorage.creatingEntry.setPlatz(intList);
    }
}
