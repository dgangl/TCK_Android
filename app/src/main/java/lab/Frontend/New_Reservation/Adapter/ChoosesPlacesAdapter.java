package lab.Frontend.New_Reservation.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import backend.Places;
import lab.tck.R;

public class ChoosesPlacesAdapter extends ArrayAdapter<Places> {
    private final LayoutInflater inflater;
    private int resource;
    private List<Places> places;
    private LinearLayout layout;

    public ChoosesPlacesAdapter(@NonNull Context context, int resource, @NonNull List<Places> places) {
        super(context, resource, places);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.places = places;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Places currentPlace = places.get(position);
        View listItem = inflater.inflate(R.layout.adapter_chooseplaces, parent, false);

        ((TextView) listItem.findViewById(R.id.adapter_place)).setText(currentPlace.getPlace());
        ((CheckBox) listItem.findViewById(R.id.adapter_takePlace)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentPlace.setTake(isChecked);
            }
        });

        layout = (LinearLayout) listItem.findViewById(R.id.places_layout);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)layout.getLayoutParams();
        params.setMargins(20, 10, 20, 10);
        layout.setLayoutParams(params);


        return listItem;

    }
}
