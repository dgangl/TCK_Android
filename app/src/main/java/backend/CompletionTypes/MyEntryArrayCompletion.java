package backend.CompletionTypes;

import java.util.List;

import backend.Database.Entry;

public interface MyEntryArrayCompletion {
    void onCallback(List<Entry> entryList);
}
