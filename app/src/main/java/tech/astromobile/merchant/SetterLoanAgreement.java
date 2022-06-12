package tech.astromobile.merchant;

public class SetterLoanAgreement {
    public SetterLoanAgreement() {
    }

    private String key, loanKey, natId;
    private boolean isAgreed;

    public SetterLoanAgreement(String key, String loanKey, String natId, boolean isAgreed) {
        this.key = key;
        this.loanKey = loanKey;
        this.natId = natId;
        this.isAgreed = isAgreed;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLoanKey() {
        return loanKey;
    }

    public void setLoanKey(String loanKey) {
        this.loanKey = loanKey;
    }

    public String getNatId() {
        return natId;
    }

    public void setNatId(String natId) {
        this.natId = natId;
    }

    public boolean isAgreed() {
        return isAgreed;
    }

    public void setAgreed(boolean agreed) {
        isAgreed = agreed;
    }
}
