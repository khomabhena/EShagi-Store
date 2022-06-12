package tech.astromobile.merchant;

public class SetterApprovalStatus {

    public SetterApprovalStatus() {
    }

    String key, natId, status, comment,officerID;
    int  otp;
    double creditLimit;
    long timestamp;

    public SetterApprovalStatus(String key, String natId, String status, String comment, String officerID, int otp, double creditLimit, long timestamp) {
        this.key = key;
        this.natId = natId;
        this.status = status;
        this.comment = comment;
        this.officerID = officerID;
        this.otp = otp;
        this.creditLimit = creditLimit;
        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNatId() {
        return natId;
    }

    public void setNatId(String natId) {
        this.natId = natId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOfficerID() {
        return officerID;
    }

    public void setOfficerID(String officerID) {
        this.officerID = officerID;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
