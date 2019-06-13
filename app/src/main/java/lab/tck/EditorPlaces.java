package lab.tck;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Backend.BackendFeedDatabase;
import Backend.LocalStorage;
import Backend.Places;
import Interfaces.MyIntArrayCompletion;

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




        BackendFeedDatabase bfd = new BackendFeedDatabase();
        bfd.freePlace(LocalStorage.creatingEntry.getDatum(), (int) LocalStorage.creatingEntry.getDauer(), new MyIntArrayCompletion() {
            @Override
            public void onCallback(List<Integer> intList) {

                System.out.println("GGA: Loaded Places!" + intList.size());
                //Listview
                listViewPlaces = findViewById(R.id.editor_placeListview);

                for (Integer i : intList){
                    places.add(new Places("Platz " + i, false));
                }

                adapter = new ChoosesPlacesAdapter(EditorPlaces.this, android.R.layout.simple_list_item_1, places);
                listViewPlaces.setAdapter(adapter);

            }
        });





        //Button
        buttonAddMembers = findViewById(R.id.editor_addMembers);
        buttonAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlatzToEntry();
                Intent intent = new Intent(EditorPlaces.this, EditorMembers.class);
                startActivity(intent);
            }
        });
    }

    private void addPlatzToEntry(){

        List<Integer> intList = new ArrayList<>();

        for (Places p : places){
            if(p.isTake()){
                Integer a = Integer.parseInt(p.getPlace().split(" ")[1]);
                intList.add(a);
            }

        }

        LocalStorage.creatingEntry.setPlatz(intList);
    }
}
