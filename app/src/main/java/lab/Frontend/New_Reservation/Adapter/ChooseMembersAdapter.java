package lab.Frontend.New_Reservation.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import Backend.Database.Person;
import lab.tck.R;

public class ChooseMembersAdapter extends ArrayAdapter<Person> {
    private final LayoutInflater inflater;
    private int resource;
    private List<Person> members;
    private LinearLayout layout;

    public ChooseMembersAdapter(@NonNull Context context, int resource, @NonNull List<Person> members) {
        super(context, resource, members);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.members = members;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Person currentPerson = members.get(position);
        View listItem = inflater.inflate(R.layout.adapter_choosemembers, parent, false);

        ((TextView) listItem.findViewById(R.id.adapter_name)).setText(currentPerson.vorname + " " + currentPerson.nachname);

        layout = (LinearLayout) listItem.findViewById(R.id.members_layout);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)layout.getLayoutParams();
        params.setMargins(20, 10, 20, 10);
        layout.setLayoutParams(params);

        return listItem;

    }
}
