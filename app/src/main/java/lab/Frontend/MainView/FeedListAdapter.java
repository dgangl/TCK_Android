package lab.Frontend.MainView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import Backend.Database.Entry;
import Backend.LocalStorage;
import Backend.CompletionTypes.MyBooleanCompletion;
import lab.Frontend.LoadingAnimation;
import lab.Frontend.New_Reservation.Activities.DetailView;
import lab.tck.R;

public class FeedListAdapter extends ArrayAdapter<Entry> {
    private final LayoutInflater inflater;
    private final Context context;
    private int resource;
    private List<Entry> eventArrayList;

    public FeedListAdapter(@NonNull Context context, int resource, @NonNull List<Entry> object) {


        super(context, resource, object);
        System.out.println("Called this function!");
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.eventArrayList = object;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        final Entry currentEntry = eventArrayList.get(position);
        final View listItem = inflater.inflate(R.layout.adapter_feedlist, parent, false);

        if(currentEntry.getUserIsIn() == -1){
            ListView.LayoutParams params = (ListView.LayoutParams) listItem.getLayoutParams();
            params.height = 0;
            listItem.setLayoutParams(params);

            return listItem;
        }


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

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LoadingAnimation la = new LoadingAnimation();
                la.startLoadingAnimation(MainActivity.cont);

                currentEntry.loadTeilnehmer(new MyBooleanCompletion() {
                    @Override
                    public void onCallback(boolean bool) {
                        LocalStorage.creatingEntry = currentEntry;

                        la.closeLoadingAnimation();
                        Intent intent = new Intent(MainActivity.cont, DetailView.class);
                        MainActivity.cont.startActivity(intent);
                    }
                });

            }
        });

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
                            .setPositiveButton("Zusagen", new DialogInterface.OnClickListener() {
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
                                    //Start Loading
                                    final LoadingAnimation loadingAnimation = new LoadingAnimation();
                                    loadingAnimation.startLoadingAnimation(MainActivity.cont);
                                    currentEntry.thinkAboutThisEvent(new MyBooleanCompletion() {
                                        @Override
                                        public void onCallback(boolean bool) {
                                            notifyDataSetChanged();
                                            //End Loading
                                            loadingAnimation.closeLoadingAnimation();
                                            System.out.println("User thinking successfully");
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Absagen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Start Loading
                                    final LoadingAnimation loadingAnimation = new LoadingAnimation();
                                    loadingAnimation.startLoadingAnimation(MainActivity.cont);
                                    currentEntry.leaveThisEvent(new MyBooleanCompletion() {
                                        @Override
                                        public void onCallback(boolean bool) {
                                            eventArrayList.remove(currentEntry);
                                            notifyDataSetChanged();
                                            //End Loading
                                            loadingAnimation.closeLoadingAnimation();
                                            System.out.println("User deleted successfully");
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