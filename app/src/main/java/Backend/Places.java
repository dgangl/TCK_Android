package Backend;

public class Places {
    private String place;
    private boolean take;

    public Places(){

    }

    public Places(String place, boolean take) {
        this.place = place;
        this.take = take;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public boolean isTake() {
        return take;
    }

    public void setTake(boolean take) {
        this.take = take;
    }
}
