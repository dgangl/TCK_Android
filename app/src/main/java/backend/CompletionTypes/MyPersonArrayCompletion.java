package backend.CompletionTypes;

import java.util.List;

import backend.Database.Person;

public interface MyPersonArrayCompletion {
    void onCallback(List<Person> personList);
}
