package tech.astromobile.merchant;

public class SetterPaymentPeriod {

    public SetterPaymentPeriod() {
    }

    private String mfiUid, key;
    private int paymentPeriod;
    private double interestRate;

    public SetterPaymentPeriod(String mfiUid, String key, int paymentPeriod, double interestRate) {
        this.mfiUid = mfiUid;
        this.key = key;
        this.paymentPeriod = paymentPeriod;
        this.interestRate = interestRate;
    }

    public String getMfiUid() {
        return mfiUid;
    }

    public void setMfiUid(String mfiUid) {
        this.mfiUid = mfiUid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(int paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
