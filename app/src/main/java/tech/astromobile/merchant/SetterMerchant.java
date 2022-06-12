package tech.astromobile.merchant;

public class SetterMerchant {

    public SetterMerchant() {
    }


    private String name, bank, licenceNumber;
    private int otp;
    private long accountNumber, timestamp;
    private boolean offeringCoupons;

    public SetterMerchant(String name, String bank, long accountNumber, String licenceNumber,
                          int otp, long timestamp, boolean offeringCoupons) {
        this.name = name;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.licenceNumber = licenceNumber;
        this.otp = otp;
        this.timestamp = timestamp;
        this.offeringCoupons = offeringCoupons;
    }

    public boolean isOfferingCoupons() {
        return offeringCoupons;
    }

    public void setOfferingCoupons(boolean offeringCoupons) {
        this.offeringCoupons = offeringCoupons;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
