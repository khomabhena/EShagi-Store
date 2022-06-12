package tech.astromobile.merchant;

public class SetterBankCharges {

    public SetterBankCharges() {
    }

    private String uid;
    private float adminFees, lifeInsurance, taxPrincipalAmount;

    public SetterBankCharges(String uid, float adminFees, float lifeInsurance, float taxPrincipalAmount) {
        this.uid = uid;
        this.adminFees = adminFees;
        this.lifeInsurance = lifeInsurance;
        this.taxPrincipalAmount = taxPrincipalAmount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public float getAdminFees() {
        return adminFees;
    }

    public void setAdminFees(float adminFees) {
        this.adminFees = adminFees;
    }

    public float getLifeInsurance() {
        return lifeInsurance;
    }

    public void setLifeInsurance(float lifeInsurance) {
        this.lifeInsurance = lifeInsurance;
    }

    public float getTaxPrincipalAmount() {
        return taxPrincipalAmount;
    }

    public void setTaxPrincipalAmount(float taxPrincipalAmount) {
        this.taxPrincipalAmount = taxPrincipalAmount;
    }
}
