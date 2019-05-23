package Backend;

import com.google.firebase.firestore.DocumentReference;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class Person {
    public String vorname;
    public String nachname;
    public String nummer;
    public boolean mitglied;
    public DocumentReference reference;

    public Person(String vorname, String nachname, String nummer, boolean mitglied, DocumentReference reference) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.nummer = nummer;
        this.mitglied = mitglied;
        this.reference = reference;
    }


    public String createCsvString(){
        return vorname + ";" + nachname + ";" + nummer + ";" + mitglied;
    }


}
