package Backend.CompletionTypes;

import java.util.List;

import Backend.Database.Entry;

public interface MyEntryArrayCompletion {
    void onCallback(List<Entry> entryList);
}
