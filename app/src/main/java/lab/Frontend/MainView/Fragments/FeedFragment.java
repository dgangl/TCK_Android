package lab.Frontend.MainView.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import Backend.Database.BackendFeedDatabase;
import Backend.Database.Entry;
import Backend.CompletionTypes.MyEntryArrayCompletion;
import lab.Frontend.MainView.MainActivity;
import lab.Frontend.MainView.FeedListAdapter;
import lab.Frontend.LoadingAnimation;
import lab.tck.R;

public class FeedFragment extends Fragment {

    private FloatingActionButton reload;

    private ListView feedList;
    private ImageView emptyImage;
    private TextView emptyText;
    private FeedListAdapter feedListAdapter;
    private List<Entry> eventArrayList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_feed, null);
        feedList = root.findViewById(R.id.feeds);

        emptyImage = root.findViewById(R.id.emptyImageView);
        emptyText = root.findViewById(R.id.emptyTextView);
        reload = root.findViewById(R.id.reloadButton);

        System.out.println("Test2 Startet");
        eventArrayList = new ArrayList<>();

        reload.setOnClickListener(view -> {
            final LoadingAnimation loadingAnimation = new LoadingAnimation();
            loadingAnimation.startLoadingAnimation(MainActivity.cont);
            BackendFeedDatabase be = new BackendFeedDatabase();
            be.loadAllEvents(entryList -> {
                System.out.println("AAC" + entryList.size());
                List<Entry> newList = new ArrayList<>();

                for (int i = 0; i < entryList.size(); i++) {
                    if (entryList.get(i).getDatum().after(new Date())) {
                        newList.add(entryList.get(i));
                    }
                }

                Collections.sort(newList, (o1, o2) -> o1.getDatum().compareTo(o2.getDatum()));


                feedListAdapter = new FeedListAdapter(MainActivity.cont, android.R.layout.simple_list_item_1, newList);
                feedList.setAdapter(feedListAdapter);


                loadingAnimation.closeLoadingAnimation();

                if (!newList.isEmpty()) {
                    emptyImage.setVisibility(emptyImage.INVISIBLE);
                    emptyText.setVisibility(emptyText.INVISIBLE);
                }

            });
        });


        final LoadingAnimation loadingAnimation = new LoadingAnimation();
        loadingAnimation.startLoadingAnimation(MainActivity.cont);
        BackendFeedDatabase be = new BackendFeedDatabase();
        be.loadAllEvents(entryList -> {
            System.out.println("AAC" + entryList.size());
            List<Entry> newList = new ArrayList<>();

            for (int i = 0; i < entryList.size(); i++) {
                if (entryList.get(i).getDatum().after(new Date())) {
                    newList.add(entryList.get(i));
                }
            }

            Collections.sort(newList, (o1, o2) -> o1.getDatum().compareTo(o2.getDatum()));


            feedListAdapter = new FeedListAdapter(MainActivity.cont, android.R.layout.simple_list_item_1, newList);
            feedList.setAdapter(feedListAdapter);


            loadingAnimation.closeLoadingAnimation();

            if (!newList.isEmpty()) {
                emptyImage.setVisibility(emptyImage.INVISIBLE);
                emptyText.setVisibility(emptyText.INVISIBLE);
            }

        });

        return root;
    }


}
