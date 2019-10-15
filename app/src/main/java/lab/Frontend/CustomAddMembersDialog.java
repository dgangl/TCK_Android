package lab.Frontend;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import Backend.CompletionTypes.MyPersonArrayCompletion;
import Backend.Database.Person;
import Backend.LocalStorage;
import lab.tck.R;

public class CustomAddMembersDialog extends Dialog implements android.view.View.OnClickListener {

    public Context c;
    public Button cancel;
    public ListView listView;
    public List<Person> members = new ArrayList<>();
    public List<Person> choosenMembers;
    public CustomAddMembersDialogAdapter adapter;
    public SearchView search;


    public CustomAddMembersDialog(Context context, List<Person> choosenMembers) {
        super(context);
        this.c = context;
        this.choosenMembers = choosenMembers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_add_members_dialog);


        cancel = findViewById(R.id.custom_add_memebers_dialog_finished_button);
        listView = findViewById(R.id.custom_add_memebers_dialog_listview);
        search = findViewById(R.id.custom_add_memebers_dialog_searchview);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // do something on text submit

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // do something when text changes
                List<Person> filtered = new ArrayList<>();
                for (Person p:members) {
                    if((p.vorname + " " + p.nachname).contains(newText)){
                        filtered.add(p);

                    }
                }
                adapter = new CustomAddMembersDialogAdapter(c, android.R.layout.simple_list_item_1, filtered, choosenMembers);
                listView.setAdapter(adapter);
                return false;
            }
        });

        Person.loadAll(new MyPersonArrayCompletion() {
            @Override
            public void onCallback(List<Person> personList) {
                Person currentUser = LocalStorage.loadUser();

                for (Person p : personList) {
                    if (!p.nummer.equals(currentUser.nummer)) {
                        members.add(p);
                        Log.e(currentUser.nummer, "!!!");

                    }
                }

                adapter = new CustomAddMembersDialogAdapter(c, android.R.layout.simple_list_item_1, members, choosenMembers);
                listView.setAdapter(adapter);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
