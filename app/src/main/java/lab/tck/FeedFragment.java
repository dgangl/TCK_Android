package lab.tck;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import Backend.BackendFeedDatabase;
import Backend.Entry;
import Interfaces.MyEntryArrayInterface;

public class FeedFragment extends Fragment {
private ListView feedList;
private FeedListAdapter feedListAdapter;
private List<Entry> eventArrayList;
private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_feed, null);
        feedList = root.findViewById(R.id.feeds);

        System.out.println("Test2 Startet");
        eventArrayList = new ArrayList<>();


        final LoadingAnimation loadingAnimation = new LoadingAnimation();
        loadingAnimation.startLoadingAnimation(MainActivity.cont);
        BackendFeedDatabase be = new BackendFeedDatabase();
        be.loadAllEvents(new MyEntryArrayInterface() {
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
            }
        });

        return root;
    }




}
