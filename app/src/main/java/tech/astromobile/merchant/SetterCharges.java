package tech.astromobile.merchant;

public class SetterCharges {

    public SetterCharges() {
    }

    private double serviceCharge, itemProcessing, lessThan40, percentForAbove40;

    public SetterCharges(double serviceCharge, double itemProcessing, double lessThan40, double percentForAbove40) {
        this.serviceCharge = serviceCharge;
        this.itemProcessing = itemProcessing;
        this.lessThan40 = lessThan40;
        this.percentForAbove40 = percentForAbove40;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public double getItemProcessing() {
        return itemProcessing;
    }

    public void setItemProcessing(double itemProcessing) {
        this.itemProcessing = itemProcessing;
    }

    public double getLessThan40() {
        return lessThan40;
    }

    public void setLessThan40(double lessThan40) {
        this.lessThan40 = lessThan40;
    }

    public double getPercentForAbove40() {
        return percentForAbove40;
    }

    public void setPercentForAbove40(double percentForAbove40) {
        this.percentForAbove40 = percentForAbove40;
    }
}
