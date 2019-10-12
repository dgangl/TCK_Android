package lab.Frontend.MainView.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Backend.Database.BackendFeedDatabase;
import Backend.Database.Entry;
import Backend.CompletionTypes.MyEntryArrayCompletion;
import lab.Frontend.MainView.MainActivity;
import lab.Frontend.MainView.FeedListAdapter;
import lab.Frontend.LoadingAnimation;
import lab.tck.R;

public class FeedFragment extends Fragment {
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

        System.out.println("Test2 Startet");
        eventArrayList = new ArrayList<>();



        final LoadingAnimation loadingAnimation = new LoadingAnimation();
        loadingAnimation.startLoadingAnimation(MainActivity.cont);
        BackendFeedDatabase be = new BackendFeedDatabase();
        be.loadAllEvents(new MyEntryArrayCompletion() {
            @Override
            public void onCallback(List<Entry> entryList) {
                System.out.println("AAC" +entryList.size());

                Collections.sort(entryList, new Comparator<Entry>() {
                    @Override
                    public int compare(Entry o1, Entry o2) {
                        return o1.getDatum().compareTo(o2.getDatum());
                    }
                });


                feedListAdapter = new FeedListAdapter(MainActivity.cont, android.R.layout.simple_list_item_1, entryList);
                feedList.setAdapter(feedListAdapter);


                loadingAnimation.closeLoadingAnimation();

                if(entryList != null || !entryList.isEmpty()){
                    emptyImage.setVisibility(emptyImage.INVISIBLE);
                    emptyText.setVisibility(emptyText.INVISIBLE);
                }

            }
        });

        return root;
    }




}
