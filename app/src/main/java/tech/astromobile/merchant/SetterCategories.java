package tech.astromobile.merchant;

public class SetterCategories {

    public SetterCategories() {
    }

    private String key, name;
    private int position;
    private boolean isAvailable;

    public SetterCategories(String key, String name, int position, boolean isAvailable) {
        this.key = key;
        this.name = name;
        this.position = position;
        this.isAvailable = isAvailable;
    }


    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
