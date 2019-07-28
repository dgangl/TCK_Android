package Backend.CompletionTypes;

import java.util.List;

import Backend.Database.Person;

public interface MyPersonArrayCompletion {
    void onCallback(List<Person> personList);
}
