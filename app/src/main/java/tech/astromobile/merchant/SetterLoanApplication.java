package tech.astromobile.merchant;

public class SetterLoanApplication {

    public SetterLoanApplication() {
    }

    private String key, microfinanceUid, clientOfficerUid, clientUid, bankName, timeStamp, isApproved, merchantName, orderKey;
    private long bankAccount, ecocash, paybackPeriod, timestampApply;
    private boolean isFullyPaid, isBankDisbursement, isMerchant;
    private int pendingBalance;
    private double amount, monthlyPayment, creditLimit, interestRate;

    public SetterLoanApplication(String key, String microfinanceUid, String clientOfficerUid, String clientUid,
                                 String bankName, String orderKey,
                                 double amount, double monthlyPayment, double creditLimit,
                                 long bankAccount, long ecocash,
                                 String isApproved, boolean isFullyPaid, boolean isBankDisbursement,
                                 long paybackPeriod, int pendingBalance,
                                 long timestampApply, String timeStamp, double interestRate,
                                 boolean isMerchant, String merchantName) {
        this.key = key;
        this.merchantName = merchantName;
        this.isMerchant = isMerchant;
        this.microfinanceUid = microfinanceUid;
        this.clientOfficerUid = clientOfficerUid;
        this.clientUid = clientUid;
        this.bankName = bankName;
        this.orderKey = orderKey;
        this.amount = amount;
        this.monthlyPayment = monthlyPayment;
        this.creditLimit = creditLimit;
        this.bankAccount = bankAccount;
        this.ecocash = ecocash;
        this.isApproved = isApproved;
        this.isFullyPaid = isFullyPaid;
        this.isBankDisbursement = isBankDisbursement;
        this.paybackPeriod = paybackPeriod;
        this.pendingBalance = pendingBalance;
        this.timeStamp = timeStamp;
        this.timestampApply = timestampApply;
        this.interestRate = interestRate;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public boolean isMerchant() {
        return isMerchant;
    }

    public void setMerchant(boolean merchant) {
        isMerchant = merchant;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMicrofinanceUid() {
        return microfinanceUid;
    }

    public void setMicrofinanceUid(String microfinanceUid) {
        this.microfinanceUid = microfinanceUid;
    }

    public String getClientOfficerUid() {
        return clientOfficerUid;
    }

    public void setClientOfficerUid(String clientOfficerUid) {
        this.clientOfficerUid = clientOfficerUid;
    }

    public String getClientUid() {
        return clientUid;
    }

    public void setClientUid(String clientUid) {
        this.clientUid = clientUid;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public long getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(long bankAccount) {
        this.bankAccount = bankAccount;
    }

    public long getEcocash() {
        return ecocash;
    }

    public void setEcocash(long ecocash) {
        this.ecocash = ecocash;
    }

    public long getPaybackPeriod() {
        return paybackPeriod;
    }

    public void setPaybackPeriod(long paybackPeriod) {
        this.paybackPeriod = paybackPeriod;
    }

    public long getTimestampApply() {
        return timestampApply;
    }

    public void setTimestampApply(long timestampApply) {
        this.timestampApply = timestampApply;
    }

    public String isApproved() {
        return isApproved;
    }

    public void setApproved(String approved) {
        isApproved = approved;
    }

    public boolean isFullyPaid() {
        return isFullyPaid;
    }

    public void setFullyPaid(boolean fullyPaid) {
        isFullyPaid = fullyPaid;
    }

    public boolean isBankDisbursement() {
        return isBankDisbursement;
    }

    public void setBankDisbursement(boolean bankDisbursement) {
        isBankDisbursement = bankDisbursement;
    }

    public int getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(int pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }
}
