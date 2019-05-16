package lab.tck;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

import Interfaces.MyBooleanInterface;

public class Entry {
    private Date datum;
    private String beschreibung;
    private List<Person> teilnemer;//teilnehmer
    private double dauer;//dauer
    private boolean privat;
    private int platz;
    private String id;
    private String type;

    private int userIsIn = -1;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Entry(Date datum, String beschreibung, List<Person> teilnemer, double dauer, boolean privat, int platz, String id, String type) {
        this.datum = datum;
        this.beschreibung = beschreibung;
        this.teilnemer = teilnemer;
        this.dauer = dauer;
        this.privat = privat;
        this.platz = platz;
        this.id = id;
        this.type = type;
    }

    public void addTeilnehmer(Person person){
        if(teilnemer == null){
            teilnemer = new ArrayList<Person>();
        }

        teilnemer.add(person);
    }

    public void loadTeilnehmer(final MyBooleanInterface callback){
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

    private void editInState(int isIn, MyBooleanInterface completion){
        userIsIn = isIn;


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
}
