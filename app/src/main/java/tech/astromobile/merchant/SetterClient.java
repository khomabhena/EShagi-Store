package tech.astromobile.merchant;

public class SetterClient {

    public SetterClient() {
    }

    private String uid, agentUid, title, name, natId, countryOfBirthUid, surname, maidenSurname, email,
            maritalStatus, passPhotoPath, payslipPath, natIdPath,nationalIdPath;
    private boolean isMale, isUploaded, isZimResident;
    private int numberOfDependants, salary;
    private long dob, phoneNumber, homeTelephone;

    public SetterClient(String uid, String agentUid, String title, String name, String natId, String countryOfBirthUid, String surname, String maidenSurname, String email, String maritalStatus, String passPhotoPath, String payslipPath, String natIdPath, String nationalIdPath, boolean isMale, boolean isUploaded, boolean isZimResident, int numberOfDependants, int salary, long dob, long phoneNumber, long homeTelephone) {
        this.uid = uid;
        this.agentUid = agentUid;
        this.title = title;
        this.name = name;
        this.natId = natId;
        this.countryOfBirthUid = countryOfBirthUid;
        this.surname = surname;
        this.maidenSurname = maidenSurname;
        this.email = email;
        this.maritalStatus = maritalStatus;
        this.passPhotoPath = passPhotoPath;
        this.payslipPath = payslipPath;
        this.natIdPath = natIdPath;
        this.nationalIdPath = nationalIdPath;
        this.isMale = isMale;
        this.isUploaded = isUploaded;
        this.isZimResident = isZimResident;
        this.numberOfDependants = numberOfDependants;
        this.salary = salary;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.homeTelephone = homeTelephone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAgentUid() {
        return agentUid;
    }

    public void setAgentUid(String agentUid) {
        this.agentUid = agentUid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNatId() {
        return natId;
    }

    public void setNatId(String natId) {
        this.natId = natId;
    }

    public String getCountryOfBirthUid() {
        return countryOfBirthUid;
    }

    public void setCountryOfBirthUid(String countryOfBirthUid) {
        this.countryOfBirthUid = countryOfBirthUid;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMaidenSurname() {
        return maidenSurname;
    }

    public void setMaidenSurname(String maidenSurname) {
        this.maidenSurname = maidenSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPassPhotoPath() {
        return passPhotoPath;
    }

    public void setPassPhotoPath(String passPhotoPath) {
        this.passPhotoPath = passPhotoPath;
    }

    public String getPayslipPath() {
        return payslipPath;
    }

    public void setPayslipPath(String payslipPath) {
        this.payslipPath = payslipPath;
    }

    public String getNatIdPath() {
        return natIdPath;
    }

    public void setNatIdPath(String natIdPath) {
        this.natIdPath = natIdPath;
    }

    public String getNationalIdPath() {
        return nationalIdPath;
    }

    public void setNationalIdPath(String nationalIdPath) {
        this.nationalIdPath = nationalIdPath;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public boolean isZimResident() {
        return isZimResident;
    }

    public void setZimResident(boolean zimResident) {
        isZimResident = zimResident;
    }

    public int getNumberOfDependants() {
        return numberOfDependants;
    }

    public void setNumberOfDependants(int numberOfDependants) {
        this.numberOfDependants = numberOfDependants;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public long getDob() {
        return dob;
    }

    public void setDob(long dob) {
        this.dob = dob;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getHomeTelephone() {
        return homeTelephone;
    }

    public void setHomeTelephone(long homeTelephone) {
        this.homeTelephone = homeTelephone;
    }
}
