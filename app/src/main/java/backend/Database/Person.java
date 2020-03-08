package backend.Database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import backend.LocalStorage;
import backend.CompletionTypes.MyPersonArrayCompletion;

public class Person {
    //Personal Data
    public String vorname;
    public String nachname;
    public String nummer;

    //Member Data
    public boolean mitglied;
    public int guthaben;

    //Database Variables
    public DocumentReference reference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Person(String vorname, String nachname, String nummer, boolean mitglied, DocumentReference reference) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.nummer = nummer;
        this.mitglied = mitglied;
        this.reference = reference;
    }


    static public void loadAll(final MyPersonArrayCompletion completion) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot query) {
                        if (query == null || query.getDocuments().size() < 1) {
                            completion.onCallback(new ArrayList<Person>());
                            return;
                        } else {
                            List<Person> mitglieder = new ArrayList<>();
                            for (DocumentSnapshot doc : query.getDocuments()) {


                                Person p = null;

                                Map<String, Object> map = doc.getData();
                                try {
                                    p = new Person(
                                            (String) map.get("vorname"),
                                            (String) map.get("nachname"),
                                            doc.getId(),
                                            (Boolean) map.get("mitglied"),
                                            doc.getReference());
                                } catch (Exception e) {
                                    System.out.println("COULDNT GET THE USER");
                                }
                                if (p != null) {
                                    mitglieder.add(p);
                                }
                            }
                            completion.onCallback(mitglieder);
                        }
                    }
                });
    }


    public void loginUser() {
        this.reference = db.collection("Users").document(nummer);


        final Map<String, Object> map = new TreeMap<>();
        map.put("vorname", vorname);
        map.put("nachname", nachname);


        db.collection("Users").document(nummer).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().get("particingEvents") == null) {
                    final Map<String, Integer> particingEvents = new TreeMap<>();
                    map.put("particingEvents", particingEvents);
                }


                boolean member = false;
                if(task.getResult().get("mitglied") != null){
                    //noinspection ConstantConditions
                    member = (Boolean) task.getResult().get("mitglied");
                }
                mitglied = member;
                map.put("mitglied", member);

                if(!member) {
                    Object guthabenObject = task.getResult().get("guthaben");
                    if (guthabenObject == null) {
                        guthaben = 0;
                        map.put("guthaben", 0);
                    } else {
                        guthaben = ((Long) guthabenObject).intValue();
                        map.put("guthaben", guthaben);
                    }
                }

                reference.update(map).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        reference.set(map);
                    }
                });
                saveUserLocal();
            }


        });
    }


    public String createCsvString() {
        return vorname + ";" + nachname + ";" + nummer + ";" + mitglied;
    }

    public void saveUserLocal() {
        LocalStorage.saveUser(this);
    }


    public void takeGuthaben() {
        if(mitglied){
            return;
        }

        DocumentReference userRef = db.collection("Users").document(nummer);


        userRef
                .update("guthaben", guthaben-1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        guthaben = guthaben-1;
                        Log.d("INFO", "guthaben updated sucessfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("INFO", "guthaben update-failure");
                    }
                });
    }
}
