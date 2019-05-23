package Backend;

public class LocalStorage {
    private static Person currentUser;

    static public void saveUser(Person person){
        currentUser = person;
    }

    static public Person loadUser(){
        return currentUser;
    }
}
