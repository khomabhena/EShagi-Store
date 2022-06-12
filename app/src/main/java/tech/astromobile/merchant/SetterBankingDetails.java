package tech.astromobile.merchant;

public class SetterBankingDetails {

    public SetterBankingDetails() {
    }

    private String key, clientUid, bankName, accountName, branchName, accountType;
    private int branchCode;
    private long accountNo;

    public SetterBankingDetails(String key, String clientUid, String bankName, String accountName, String branchName, String accountType, int branchCode, long accountNo) {
        this.key = key;
        this.clientUid = clientUid;
        this.bankName = bankName;
        this.accountName = accountName;
        this.branchName = branchName;
        this.accountType = accountType;
        this.branchCode = branchCode;
        this.accountNo = accountNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(int branchCode) {
        this.branchCode = branchCode;
    }

    public long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(long accountNo) {
        this.accountNo = accountNo;
    }
}