package lab.tck;

import com.google.firebase.firestore.DocumentReference;

public class Person {
    private String vorname;
    private String nachname;
    private String nummer;
    private boolean mitglied;
    private DocumentReference reference;

    public Person(String vorname, String nachname, String nummer, boolean mitglied, DocumentReference reference) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.nummer = nummer;
        this.mitglied = mitglied;
        this.reference = reference;
    }


}
