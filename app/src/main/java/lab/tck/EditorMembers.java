package lab.tck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Backend.Person;
import Backend.Places;

public class EditorMembers extends AppCompatActivity {
    private Button buttonCheckEntry;
    private EditText editTextDescription;
    private ListView listViewMembers;
    private List<Person> members = new ArrayList<>();
    private ChooseMembersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_members);

        //Actionbar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //Toolbar
        Toolbar mToolbar =  findViewById(R.id.toolbar3);

        mToolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //UI
        buttonCheckEntry = findViewById(R.id.editor_checkEntry);
        editTextDescription = findViewById(R.id.editor_description);
        listViewMembers = findViewById(R.id.editor_choosenMembers);



        //ListView
        members.add(new Person("David", "Gangl", "123", true, null));
        adapter = new ChooseMembersAdapter(EditorMembers.this, android.R.layout.simple_list_item_1, members);
        listViewMembers.setAdapter(adapter);

        //Button
        buttonCheckEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //:TODO detail
                //Intent intent = new Intent(EditorMembers.this, deiklasseKren.class);
                //startActivity(intent);
            }
        });
    }
}
