package Backend;

import java.util.List;

public class ReservationReturn {
    String dayString;
    List<String> hourStrings;

    public ReservationReturn(String dayString, List<String> hourStrings) {
        this.dayString = dayString;
        this.hourStrings = hourStrings;
    }
}
