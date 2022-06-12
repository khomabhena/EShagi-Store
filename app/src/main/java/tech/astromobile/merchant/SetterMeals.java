package tech.astromobile.merchant;

public class SetterMeals {

    public SetterMeals() {
    }

    private String key, category, name, size, description, link, orderKey;
    private int limit;
    private double price, takeawayCharge;
    private boolean isAvailable, isHomeDelivery;
    private long timestamp;

    public SetterMeals(String key, String category, String name, String size, String description, String link, String orderKey,
                       int limit,
                       double price, double takeawayCharge,
                       boolean isAvailable, boolean isHomeDelivery,
                       long timestamp) {
        this.key = key;
        this.category = category;
        this.name = name;
        this.size = size;
        this.description = description;
        this.link = link;
        this.orderKey = orderKey;
        this.limit = limit;
        this.price = price;
        this.takeawayCharge = takeawayCharge;
        this.isAvailable = isAvailable;
        this.isHomeDelivery = isHomeDelivery;
        this.timestamp = timestamp;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTakeawayCharge() {
        return takeawayCharge;
    }

    public void setTakeawayCharge(double takeawayCharge) {
        this.takeawayCharge = takeawayCharge;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isHomeDelivery() {
        return isHomeDelivery;
    }

    public void setHomeDelivery(boolean homeDelivery) {
        isHomeDelivery = homeDelivery;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
