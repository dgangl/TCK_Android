package Backend.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import Backend.LocalStorage;
import Backend.CompletionTypes.MyBooleanCompletion;

public class Entry {
    private Date datum;
    private String beschreibung;
    private List<Person> teilnemer;//teilnehmer
    private double dauer;//dauer
    private boolean privat;
    private List<Integer> platz;
    private String id;
    private String type;

    public int userIsIn = -1;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Entry(){

    }

    public Entry(Date datum, String beschreibung, List<Person> teilnemer, double dauer, boolean privat, List<Long> platz, String id, String type) {
        this.datum = datum;
        this.beschreibung = beschreibung;
        this.teilnemer = teilnemer;
        this.dauer = dauer;
        this.privat = privat;

        List<Integer> intList = new ArrayList<>();
        for (Long l : platz){
            intList.add(l.intValue());
        }

        this.platz = intList;
        this.id = id;
        this.type = type;
    }

    public Entry(String id){
        this.id = id;
    }

    public void addTeilnehmer(Person person){
        if(teilnemer == null){
            teilnemer = new ArrayList<Person>();
        }

        teilnemer.add(person);
    }

    public void loadTeilnehmer(final MyBooleanCompletion callback){
        if(teilnemer != null){
            callback.onCallback(true);
            return;
        }else{
            teilnemer = new ArrayList<>();
        }

        if(id != null){
            db.collection("Entrys").document(id)
                    .collection("teilnehmer")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    teilnemer.add(documentToObject(document.getData()));
                                }
                                callback.onCallback(true);
                            } else {
                                callback.onCallback(false);
                            }
                        }
                    });
        }
    }


    public void deleteTeilnehmer(Person person){
        if(teilnemer == null){
            return;
        }else{
            teilnemer.remove(person);
        }
    }

    private void editInState(final int isIn, final MyBooleanCompletion completion){
        userIsIn = isIn;

        if(id == null || LocalStorage.loadUser() == null){
            return;
        }

        final Person user = LocalStorage.loadUser();
        addEventToUser(db.collection("Entrys").document(id), user, isIn);

        final CollectionReference teilnehmerReference = db.collection("Entrys").document(id).collection("teilnehmer");

        Query personReference = teilnehmerReference.whereEqualTo("number", user.nummer);

        personReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                System.out.println("DDC: Onclomplete");
                if (snapshot != null){
                    if(snapshot.getDocuments().size() == 0){

                        Map<String, Object> map =  new TreeMap<String, Object>();
                        map.put("isIn", isIn);
                        map.put("Comment", "");
                        map.put("isAdmin", true);
                        map.put("vorname", user.vorname);
                        map.put("nachname", user.nachname);
                        map.put("number", user.nummer);


                        teilnehmerReference.add(map)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        completion.onCallback(true);
                                    }
                                });
                    }

                    for (DocumentSnapshot doc : snapshot.getDocuments()){

                        doc.getReference().update("isIn", isIn)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        completion.onCallback(true);
                                    }
                                });
                    }


                }
                completion.onCallback(false);
            }
        });

    }

    public void joinThisEvent(MyBooleanCompletion completion){
        editInState(1, completion);
    }

    public void leaveThisEvent(MyBooleanCompletion completion){
        editInState(-1, completion);
    }

    public void thinkAboutThisEvent(MyBooleanCompletion completion){
        editInState(0, completion);
    }

    public void uploadToDatabase(final MyBooleanCompletion completion){


        db.collection("Entrys")
                .add(createDictionary())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        extendedUploadToDatabase(documentReference);
                        completion.onCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completion.onCallback(false);
                    }
                });
    }

    private void extendedUploadToDatabase(DocumentReference ref){
        Person currentUser = LocalStorage.loadUser();

        //Add the Admin
        Map<String, Object> teilnehmerMap = new TreeMap<>();
        teilnehmerMap.put("isIn", 1);
        teilnehmerMap.put("Comment", "");
        teilnehmerMap.put("isAdmin", true);
        teilnehmerMap.put("vorname", currentUser.vorname);
        teilnehmerMap.put("nachname", currentUser.nachname);
        teilnehmerMap.put("number", currentUser.nummer);

        ref.collection("teilnehmer").add(teilnehmerMap);

        if(currentUser != null){
            addEventToUser(ref, currentUser, 1);
        }

        if(teilnemer == null){
            teilnemer = new ArrayList<>();
        }
        //Add all the Others
        for (Person person : teilnemer){

            if(person.nummer == currentUser.nummer){
                continue;
            }

            Map<String, Object> othersMap = new TreeMap<>();
            othersMap.put("isIn", 0);
            othersMap.put("Comment", "");
            othersMap.put("isAdmin", false);
            othersMap.put("vorname", person.vorname);
            othersMap.put("nachname", person.nachname);
            othersMap.put("number", person.nummer);

            ref.collection("teilnehmer").add(othersMap);

            addEventToUser(ref, person, 0);
        }

        final ReservationReturn values = BackendFeedDatabase.createValuesForReservation(datum, (int) dauer);
        final Map<String, List<Integer>> data = new TreeMap<>();

        for (String hour : values.hourStrings){
            data.remove(hour);
            data.put(hour, platz);
        }

        db.collection("Reservations").document(values.dayString).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> map = documentSnapshot.getData();

                if(map != null){
                    Map<String, List<Integer>> reservated = (Map<String, List<Integer>>) map.get("Reserviert");

                    reservated = mergeMap(reservated, data);

                    Map<String, Object> temp = new TreeMap<String, Object>();
                    temp.put("Reserviert", reservated);

                    db.collection("Reservations").document(values.dayString).set(temp);
                }else{
                    Map<String, Object> temp = new TreeMap<String, Object>();
                    temp.put("Reserviert", data);
                    db.collection("Reservations").document(values.dayString).set(temp);
                }


            }
        });




    }

    private Map<String, List<Integer>> mergeMap(Map<String, List<Integer>> oldMap, Map<String, List<Integer>> newMap){
        Map<String, List<Integer>> mergeMap = new TreeMap<>();

        for (String key : oldMap.keySet()) {
            if(newMap.keySet().contains(key)) {
                oldMap.get(key).addAll(newMap.get(key));
            }
            mergeMap.put(key, oldMap.get(key));
        }
        return mergeMap;
    }

    private void addEventToUser(final DocumentReference ref, final Person person, final int isIn){
        db.collection("Users")
                .document(person.nummer)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot != null && documentSnapshot.getData() != null){
                            Map<String, Object> dat = documentSnapshot.getData();

                            if(dat.containsKey("particingEvents") != true){
                                Map<String, Integer> partEventsMap = new TreeMap<>();
                                partEventsMap.put(ref.getId(), isIn);
                                Map<String, Object> sendObject = new TreeMap<>();
                                sendObject.put("particingEvents", partEventsMap);
                                db.collection("Users").document(person.nummer).set(sendObject, SetOptions.merge());
                            }else{
                                Map<String, Integer> partEvents = (Map<String, Integer>) dat.get("particingEvents");
                                partEvents.remove(ref.getId());
                                partEvents.put(ref.getId(), isIn);

                                db.collection("Users")
                                        .document(person.nummer)
                                        .update("particingEvents", partEvents);
                            }
                        }
                    }
                });
    }


    private Map<String, Object> createDictionary(){
        Map<String, Object> map = new TreeMap<>();

        map.put("beschreibung", beschreibung);
        map.put("datum", datum);
        map.put("dauer", dauer);
        map.put("privat", privat );
        map.put("platz", platz);
        map.put("type", type);

        return map;


    }

    /////////////////////
    //ANDROID FUNCTIONS//
    /////////////////////

    private Person documentToObject(Map<String, Object> map){
        return new Person(
                (String) map.get("vorname"),
                (String) map.get("nachname"),
                (String) map.get("number"),
                true,
                null
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return Objects.equals(id, entry.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public Date getDatum() {
        return datum;
    }

    public String getDateString(){

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d. MMMM");



        return sdf.format(datum);
    }
    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public List<Person> getTeilnemer() {
        return teilnemer;
    }

    public void setTeilnemer(List<Person> teilnemer) {
        this.teilnemer = teilnemer;
    }

    public double getDauer() {
        return dauer;
    }

    public void setDauer(double dauer) {
        this.dauer = dauer;
    }

    public boolean isPrivat() {
        return privat;
    }

    public void setPrivat(boolean privat) {
        this.privat = privat;
    }

    public List<Integer> getPlatz() {
        return platz;
    }

    public void setPlatz(List<Integer> platz) {
        this.platz = platz;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserIsIn() {
        return userIsIn;
    }

    public void setUserIsIn(int userIsIn) {
        this.userIsIn = userIsIn;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }
}
