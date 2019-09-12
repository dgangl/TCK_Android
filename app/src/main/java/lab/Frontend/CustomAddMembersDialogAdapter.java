package lab.Frontend;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Backend.Database.Person;
import lab.Frontend.New_Reservation.Adapter.ChooseMembersAdapter;
import lab.tck.R;

public class CustomAddMembersDialogAdapter extends ArrayAdapter<Person> {
    private final LayoutInflater inflater;
    private int resource;
    private List<Person> members;
    private ListView listViewMembers;
    public List<Person> choosenMembers = new ArrayList<>();
    private ChooseMembersAdapter adapter;
    private Context c;
    private LinearLayout layout;


    public CustomAddMembersDialogAdapter(@NonNull Context context, int resource, @NonNull List<Person> members, @NonNull List<Person> choosenMembers) {
        super(context, resource, members);
        this.c = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.members = members;
        this.choosenMembers = choosenMembers;

        for (Person p : choosenMembers) {

            Log.e(p.nummer, "CurrentChoosen!!!");


        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Person currentPerson = members.get(position);
        View listItem = inflater.inflate(R.layout.adapter_customaddmembersdialog, parent, false);

        View editormembers = inflater.inflate(R.layout.activity_editor_members, parent, false);

        layout = (LinearLayout) listItem.findViewById(R.id.dialog_layout);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)layout.getLayoutParams();
        params.setMargins(20, 10, 20, 10);
        layout.setLayoutParams(params);

        listViewMembers = ((Activity) c).findViewById(R.id.detail_choosenMembers);

        for (Person p:choosenMembers) {
            if (p.nummer.equals(currentPerson.nummer)) {
                ((CheckBox) listItem.findViewById(R.id.adapter_customDialogCheckbox)).setChecked(true);
            }
            Log.i("ahdfjfa", p.nachname + " " + p.vorname);
        }

        ((TextView) listItem.findViewById(R.id.adapter_customDialogTextView)).setText(currentPerson.vorname + " " + currentPerson.nachname);

        ((CheckBox) listItem.findViewById(R.id.adapter_customDialogCheckbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    choosenMembers.add(currentPerson);
                } else {
                    choosenMembers.remove(currentPerson);
                }
                adapter = new ChooseMembersAdapter(c, android.R.layout.simple_list_item_1, choosenMembers);
                listViewMembers.setAdapter(adapter);

                for (Person p : choosenMembers) {
                    Log.e(p.nachname + " " + p.vorname, "ChoosenMembers");
                }
            }
        });

        return listItem;

    }
}

