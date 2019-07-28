package Backend;

import Backend.Database.Entry;
import Backend.Database.Person;

public class LocalStorage {
    private static Person currentUser;
    public static Entry creatingEntry;

    static public void saveUser(Person person){
        currentUser = person;
    }

    static public Person loadUser(){
        return currentUser;
    }
}
