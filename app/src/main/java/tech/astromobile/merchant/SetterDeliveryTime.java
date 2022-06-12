package tech.astromobile.merchant;

public class SetterDeliveryTime {

    public SetterDeliveryTime() {
    }

    private String key, name;
    private int startHour, endHour, minute, position;
    private double extraCharge;
    private boolean isAvailable;

    public SetterDeliveryTime(String key, String name, int startHour, int endHour,
                              int minute, double extraCharge, boolean isAvailable, int position) {
        this.key = key;
        this.name = name;
        this.startHour = startHour;
        this.endHour = endHour;
        this.minute = minute;
        this.extraCharge = extraCharge;
        this.isAvailable = isAvailable;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(double extraCharge) {
        this.extraCharge = extraCharge;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
