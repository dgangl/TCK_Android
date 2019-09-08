package lab.Frontend.New_Reservation.Activities;

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
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Backend.LocalStorage;
import Backend.Database.Person;
import lab.Frontend.CustomAddMembersDialog;
import lab.Frontend.New_Reservation.Adapter.ChooseMembersAdapter;
import lab.tck.R;

public class EditorMembers extends AppCompatActivity {
    private Button buttonCheckEntry;
    private Button addMember;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#021B79"));
        }

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
        listViewMembers = findViewById(R.id.detail_choosenMembers);
        addMember = findViewById(R.id.editor_addMembers);


        //ListView
        members.add(LocalStorage.loadUser());
        adapter = new ChooseMembersAdapter(EditorMembers.this, android.R.layout.simple_list_item_1, members);
        listViewMembers.setAdapter(adapter);

        //Button
        buttonCheckEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocalStorage.creatingEntry.setTeilnemer(members);

                if(editTextDescription.getText().toString().isEmpty()){
                    LocalStorage.creatingEntry.setBeschreibung("Sonstiges");
                }else{
                    LocalStorage.creatingEntry.setBeschreibung(editTextDescription.getText().toString());
                }
                    Intent intent = new Intent(EditorMembers.this, DetailView.class);
                    startActivity(intent);
            }
        });

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAddMembersDialog camd = new CustomAddMembersDialog(EditorMembers.this, members);
                camd.show();
            }
        });
    }

}
