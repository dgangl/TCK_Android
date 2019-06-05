package lab.tck;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import Backend.Entry;
import Backend.Places;

public class ChoosesPlacesAdapter extends ArrayAdapter<Places> {
    private final LayoutInflater inflater;
    private int resource;
    private List<Places> places;

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
        ((CheckBox) listItem.findViewById(R.id.adapter_takePlace)).setChecked(currentPlace.isTake());


        return listItem;

    }
}
