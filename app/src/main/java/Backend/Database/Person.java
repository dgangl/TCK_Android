package Backend.Database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Backend.LocalStorage;
import Backend.CompletionTypes.MyPersonArrayCompletion;

public class Person {
    public String vorname;
    public String nachname;
    public String nummer;
    public boolean mitglied;
    public DocumentReference reference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Person(String vorname, String nachname, String nummer, boolean mitglied, DocumentReference reference) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.nummer = nummer;
        this.mitglied = mitglied;
        this.reference = reference;
    }


    static public void loadAll(final MyPersonArrayCompletion completion){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot query) {
                if(query == null || query.getDocuments().size() < 1){
                    completion.onCallback(new ArrayList<Person>());
                    return;
                }else{
                    List<Person> mitglieder = new ArrayList<>();
                    for (DocumentSnapshot doc : query.getDocuments()){




                        Person p = null;

                        Map<String, Object> map = doc.getData();
                        try {
                             p = new Person(
                                    (String) map.get("vorname"),
                                    (String) map.get("nachname"),
                                    doc.getId(),
                                    (Boolean) map.get("mitglied"),
                                    doc.getReference());
                        }catch (Exception e){
                            System.out.println("COULDNT GET THE USER");
                        }
                        if(p != null){
                            mitglieder.add(p);
                        }
                    }

                    completion.onCallback(mitglieder);

                }
            }
        });
    }


    public void loginUser(){
        this.reference = db.collection("Users").document(nummer);

        final Map<String, Object> map = new TreeMap<>();
        map.put("vorname", vorname);
        map.put("nachname", nachname);
        map.put("mitglied", mitglied);

        reference.update(map).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                reference.set(map);
            }
        });

        LocalStorage.saveUser(this);
    }


    public String createCsvString(){
        return vorname + ";" + nachname + ";" + nummer + ";" + mitglied;
    }


}
