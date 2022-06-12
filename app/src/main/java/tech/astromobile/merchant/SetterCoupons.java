package tech.astromobile.merchant;

public class SetterCoupons {

    public SetterCoupons() {
    }

    private String key, couponNumber;
    private float amount;
    private boolean available;

    public SetterCoupons(String key, float amount, boolean available, String couponNumber) {
        this.key = key;
        this.amount = amount;
        this.available = available;
        this.couponNumber = couponNumber;
    }

    public String getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
