package lab.tck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Backend.Places;

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

        //Toolbar
        Toolbar mToolbar =  findViewById(R.id.toolbar2);

        mToolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Listview
        listViewPlaces = findViewById(R.id.editor_placeListview);
        for (int i = 1; i <= 4 ; i++) {
            places.add(new Places("Platz " + i, false));
        }

        adapter = new ChoosesPlacesAdapter(EditorPlaces.this, android.R.layout.simple_list_item_1, places);
        listViewPlaces.setAdapter(adapter);

        //Button
        buttonAddMembers = findViewById(R.id.editor_addMembers);
        buttonAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorPlaces.this, EditorMembers.class);
                startActivity(intent);
            }
        });
    }
}
