package lab.tck;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FeedListAdapter extends ArrayAdapter<Event> {
    private final LayoutInflater inflater;
    private int resource;
    private ArrayList<Event> eventArrayList;

    public FeedListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Event> object) {
        super(context, resource, object);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.eventArrayList = object;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Event currentEvent = eventArrayList.get(position);
        View listItem = inflater.inflate(R.layout.adapter_feedlist, parent, false);

        //((TextView) listItem.findViewById(R.id.feed_title)).setText(currentEvent.getTitle());


        return listItem;

    }
}
