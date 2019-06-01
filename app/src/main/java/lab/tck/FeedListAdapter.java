package lab.tck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Backend.Entry;
import Interfaces.MyBooleanCompletion;

public class FeedListAdapter extends ArrayAdapter<Entry> {
    private final LayoutInflater inflater;
    private final Context context;
    private int resource;
    private List<Entry> eventArrayList;

    public FeedListAdapter(@NonNull Context context, int resource, @NonNull List<Entry> object) {
        super(context, resource, object);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.eventArrayList = object;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        final Entry currentEntry = eventArrayList.get(position);
        final View listItem = inflater.inflate(R.layout.adapter_feedlist, parent, false);

        ((TextView) listItem.findViewById(R.id.feed_title)).setText(currentEntry.getBeschreibung());
        ((TextView) listItem.findViewById(R.id.feed_description)).setText(currentEntry.getType());
        ((TextView) listItem.findViewById(R.id.feed_dateTime)).setText(currentEntry.getDateString());

        //Start and end-time
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        int start_time = Integer.valueOf(sdf.format(currentEntry.getDatum()));
        Double d = currentEntry.getDauer();

        int end_time = start_time + d.intValue();

        ((TextView) listItem.findViewById(R.id.feed_duration)).setText(start_time + ":00 - "+end_time + ":00 Uhr");

        String plaetze = "Plätze: " + "\n";
        plaetze = plaetze + currentEntry.getPlatz().get(0);

        for (int i = 1; i < currentEntry.getPlatz().size(); i++) {
            plaetze = plaetze + ", " + currentEntry.getPlatz().get(i);
        }
        Button b = listItem.findViewById(R.id.button);

        if(currentEntry.getUserIsIn() == 1){

            Space s = listItem.findViewById(R.id.space);

            System.out.println("Fundst!");

            s.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 0f));
            b.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 0f));
        }else{
            b.setBackgroundColor(Color.TRANSPARENT);
            b.setTextColor(Color.BLACK);
            plaetze = "Teilnehmen?";
            ImageView mImageView;
            mImageView = listItem.findViewById(R.id.imageView2);
            mImageView.setImageDrawable(MainActivity.image);

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Fundst! a");
                    AlertDialog.Builder bobsebuilder = new AlertDialog.Builder(MainActivity.cont)
                            .setTitle("Beitreten?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Start Loading
                                    final LoadingAnimation loadingAnimation = new LoadingAnimation();
                                    loadingAnimation.startLoadingAnimation(MainActivity.cont);
                                    currentEntry.joinThisEvent(new MyBooleanCompletion() {
                                        @Override
                                        public void onCallback(boolean bool) {
                                            notifyDataSetChanged();
                                            //End Loading
                                            loadingAnimation.closeLoadingAnimation();
                                            System.out.println("User added successfully");
                                        }
                                    });
                                }
                            })
                            .setNeutralButton("Vielleicht", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    currentEntry.thinkAboutThisEvent(new MyBooleanCompletion() {
                                        @Override
                                        public void onCallback(boolean bool) {
                                            notifyDataSetChanged();
                                            System.out.println("User thinking successfully");
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Absagen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    currentEntry.leaveThisEvent(new MyBooleanCompletion() {
                                        @Override
                                        public void onCallback(boolean bool) {
                                            notifyDataSetChanged();
                                            System.out.println("User canceled event" +bool);
                                        }
                                    });

                                }
                            });

                    bobsebuilder.show();




                }
            });


        }



        b.setText(plaetze);






        return listItem;

    }
}
