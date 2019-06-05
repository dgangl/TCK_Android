package Backend;

import android.app.usage.UsageEvents;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Interfaces.MyEntryArrayInterface;
import Interfaces.MyEntryCompletion;
import Interfaces.MyIntArrayCompletion;

public class BackendFeedDatabase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static private int counter = 0;

    public void loadAllEvents(final MyEntryArrayInterface completion){
        counter = 0;
        loadPrivateEvents(new MyEntryArrayInterface() {
            @Override
            public void onCallback(List<Entry> entryList) {
                loadPublicEvents(entryList, new MyEntryArrayInterface() {
                    @Override
                    public void onCallback(List<Entry> entryList) {
                        for (Entry e: entryList){
                            if(e.userIsIn == -1){
                                entryList.remove(e);
                            }
                        }

                        completion.onCallback(entryList);
                    }
                });
            }
        });
    }

    private void loadPublicEvents(final List<Entry> privateEvents, final MyEntryArrayInterface completion){
        final List<Entry> allEvents = privateEvents;

        System.out.println("public called");
        db.collection("Entrys")
                .whereEqualTo("privat", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot snapshot = task.getResult();
                        if(snapshot.getDocuments() != null && snapshot.getDocuments().size() > 0){

                            for (DocumentSnapshot doc : snapshot.getDocuments()){
                                boolean contains = privateEvents.contains(new Entry(doc.getId()));

                                Timestamp timestamp = doc.getTimestamp("date");
                                java.util.Date date = timestamp.toDate();

                                if(contains != true){
                                    Map<String, Object> map = doc.getData();
                                    Entry entry = new Entry(
                                            date,
                                            (String) map.get("beschreibung"),
                                            null,
                                            (Double) map.get("dauer"),
                                            false,
                                            (List<Long>) map.get("platz"),
                                            doc.getId(),
                                            (String) map.get("type")
                                    );

                                    entry.userIsIn = 0;
                                    allEvents.add(entry);
                                }
                            }
                            completion.onCallback(allEvents);

                        }else{
                            completion.onCallback(privateEvents);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("scheiße1");
                        completion.onCallback(allEvents);
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        System.out.println("canceld1e");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshot) {
                        System.out.println("succ1");
                    }
                });
    }

    private void loadPrivateEvents(final MyEntryArrayInterface completion){
        Person user = LocalStorage.loadUser();
        if(user == null || user.reference == null) {
            completion.onCallback(new ArrayList<Entry>());
            return;
        }
        user.reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot == null || documentSnapshot.getData() == null){
                    completion.onCallback(new ArrayList<Entry>());

                }else{
                    Map<String, Object> map = documentSnapshot.getData();

                    final Map<String, Long> events = (Map<String, Long>) map.get("particingEvents");
                    final List<Entry> entries = new ArrayList<>();

                    if(events == null || events.isEmpty()){
                        completion.onCallback(new ArrayList<Entry>());

                    }else{



                        for (String key : events.keySet()){
                            Integer value = events.get(key).intValue();

                            getit(key, value, new MyEntryCompletion() {
                                @Override
                                public void onCallback(Entry ent) {
                                    if(ent == null){
                                        counter++;
                                    }else{
                                        entries.add(ent);
                                        counter++;
                                    }

                                    if(counter == events.size()){
                                        completion.onCallback(entries);

                                    }
                                }
                            });
                        }}

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("FAIL!");
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                System.out.println("complete");
            }
        });
    }

    private void getit(final String key, final int value, final MyEntryCompletion completion){
        db.collection("Entrys").document(key).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot != null && documentSnapshot.getData().size() > 0){
                            Map<String,Object> dat = documentSnapshot.getData();

                            Timestamp timestamp = documentSnapshot.getTimestamp("datum");
                            java.util.Date date = timestamp.toDate();

                            Entry entry = new Entry(
                                    date,
                                    (String) dat.get("beschreibung"),
                                    null,
                                    (Double) dat.get("dauer"),
                                    false,
                                    (List<Long>) dat.get("platz"),
                                    key,
                                    (String) dat.get("type")
                            );

                            entry.userIsIn = value;
                            completion.onCallback(entry);

                        }else{
                            completion.onCallback(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completion.onCallback(null);
                    }
                });
    }

    private void freePlace(Date datum, int duration, final MyIntArrayCompletion completion){
        final Map<Integer, Boolean> freePlaces = new TreeMap<>();
        freePlaces.put(1, true);
        freePlaces.put(2, true);
        freePlaces.put(3, true);
        freePlaces.put(4, true);

        ReservationReturn values = createValuesForReservation(datum, duration);

        String dayString = values.dayString;
        final List<String> hourStrings = values.hourStrings;

        db.collection("Reservations").document(dayString).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot != null && documentSnapshot.getData() != null){
                            Map<String, List<Integer>> reservations = (Map<String, List<Integer>>) documentSnapshot.getData().get("Reserviert");

                            for (String hour : hourStrings){

                                List<Integer> reservation = reservations.get(hour);

                                for (Integer place : reservation){
                                    freePlaces.remove(place);
                                    freePlaces.put(place, false);
                                }


                            }

                            completion.onCallback(freePlacesMapToArray(freePlaces));
                        }else{
                            completion.onCallback(freePlacesMapToArray(freePlaces));
                        }
                    }
                });
    }

    public static ReservationReturn createValuesForReservation(Date date, Integer duration){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        String dayString = formatter.format(date);

        formatter = new SimpleDateFormat("HH");
        int firstHour = Integer.valueOf(formatter.format(date));

        List<String> hourStrings = new ArrayList<>();

        for (int i = 0; i < duration; i++) {
            hourStrings.add(""+(firstHour +i));
        }

        return new ReservationReturn(dayString, hourStrings);
    }

    private List<Integer> freePlacesMapToArray(Map<Integer, Boolean> map){
        List<Integer> returnArray = new ArrayList<>();

        for(Integer key : map.keySet()){
            if(map.get(key) == true){
                returnArray.add(key);
            }
        }

        return returnArray;
    }

}
