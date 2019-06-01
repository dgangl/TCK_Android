package lab.tck;

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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_feed, null);
        feedList = root.findViewById(R.id.feeds);

        System.out.println("Test2 Startet");
        eventArrayList = new ArrayList<>();

        BackendFeedDatabase be = new BackendFeedDatabase();
        be.loadAllEvents(new MyEntryArrayInterface() {
            @Override
            public void onCallback(List<Entry> entryList) {
                System.out.println("Test3" +entryList.size());
            }
        });

        BackendFeedDatabase bfd = new BackendFeedDatabase();
        bfd.loadAllEvents(new MyEntryArrayInterface() {
            @Override
            public void onCallback(List<Entry> entryList) {
                System.out.println("Test2 " + entryList.size());
                eventArrayList =  entryList;
            }
        });

        feedListAdapter = new FeedListAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, eventArrayList);
        feedList.setAdapter(feedListAdapter);
        return root;
    }




}
